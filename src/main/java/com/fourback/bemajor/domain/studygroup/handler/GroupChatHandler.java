package com.fourback.bemajor.domain.studygroup.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourback.bemajor.domain.studygroup.dto.IncomingGroupChatMessageDto;
import com.fourback.bemajor.domain.studygroup.dto.OutgoingGroupChatMessageDto;
import com.fourback.bemajor.domain.studygroup.entity.GroupChatMessageEntity;
import com.fourback.bemajor.domain.studygroup.entity.StudyGroup;
import com.fourback.bemajor.domain.studygroup.entity.StudyJoined;
import com.fourback.bemajor.domain.studygroup.repository.GroupChatMessageRepository;
import com.fourback.bemajor.domain.studygroup.repository.StudyGroupRepository;
import com.fourback.bemajor.domain.studygroup.repository.StudyJoinedRepository;
import com.fourback.bemajor.domain.studygroup.service.GroupChatMessageService;
import com.fourback.bemajor.global.common.enums.RedisKeyPrefixEnum;
import com.fourback.bemajor.global.common.service.FcmService;
import com.fourback.bemajor.global.common.service.RedisService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GroupChatHandler extends TextWebSocketHandler {
    private final FcmService fcmService;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;
    private final StudyJoinedRepository studyJoinedRepository;
    private final GroupChatMessageService groupChatMessageService;
    private final GroupChatMessageRepository groupChatMessageRepository;
    private final Map<Long, Set<WebSocketSession>> sessionsByStudyGroupId;
    private final Map<WebSocketSession, Pair<Long, Long>> UserGroupIdsBySession = new ConcurrentHashMap<>();


    @Override
    @Transactional
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        Long userId = (Long) session.getAttributes().get("userId");
        Long studyGroupId = Long.valueOf(Objects.requireNonNull(session.getUri())
                .getQuery().split("&")[0].split("=")[1]);

        UserGroupIdsBySession.put(session, Pair.of(userId, studyGroupId));
        sessionsByStudyGroupId.get(studyGroupId).add(session);

        updateDisConnectedUserInRedis(userId, studyGroupId);

        sendPendingMessages(session, userId, studyGroupId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        Pair<Long, Long> ids = UserGroupIdsBySession.get(session);

        Long senderId = ids.getLeft();
        Long studyGroupId = ids.getRight();

        IncomingGroupChatMessageDto incomingMessageDto =
                objectMapper.readValue(payload, IncomingGroupChatMessageDto.class);

        OutgoingGroupChatMessageDto outgoingMessageDto =
                incomingMessageDto.toOutgoingMessageDto(senderId, LocalDateTime.now());

        sendMessageToActiveSessions(studyGroupId, outgoingMessageDto);

        saveAndNotifyDisconnectedUsers(studyGroupId, outgoingMessageDto, incomingMessageDto);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Pair<Long, Long> ids = UserGroupIdsBySession.get(session);

        Long userId = ids.getLeft();
        Long studyGroupId = ids.getRight();

        Boolean isAlarmSet = Boolean.valueOf(Objects.requireNonNull(session.getUri())
                .getQuery().split("&")[1].split("=")[1]);

        redisService.putLongBooleanField(RedisKeyPrefixEnum.DISCONNECTED,
                studyGroupId, userId, isAlarmSet);

        sessionsByStudyGroupId.get(studyGroupId).remove(session);
        UserGroupIdsBySession.remove(session);
    }

    private void sendPendingMessages(WebSocketSession session, Long userId, Long studyGroupId) throws IOException {
        List<GroupChatMessageEntity> groupChatMessages = groupChatMessageRepository
                .findAllByReceiverIdAndStudyGroupId(userId, studyGroupId);

        List<OutgoingGroupChatMessageDto> outgoingMessageDtoList = groupChatMessages.stream().map(
                GroupChatMessageEntity::toOutgoingMessageDto).toList();

        if (!outgoingMessageDtoList.isEmpty()) {
            for (OutgoingGroupChatMessageDto outgoingMessageDto : outgoingMessageDtoList) {
                sendMessage(session, outgoingMessageDto);
            }

            groupChatMessageRepository.deleteAllInBatch(groupChatMessages);
        }
    }

    private void updateDisConnectedUserInRedis(Long userId, Long studyGroupId) {
        if (!redisService.checkKey(RedisKeyPrefixEnum.DISCONNECTED, studyGroupId)) {
            List<StudyJoined> joinedList = studyJoinedRepository
                    .findAllByUserIdNotAndStudyGroupIdWithUser(userId, studyGroupId);

            Map<Long, Boolean> alarmSetByUserId = joinedList.stream().collect(Collectors.toMap(
                    studyJoined -> studyJoined.getUser().getId(), StudyJoined::getIsAlarmSet));

            redisService.putLongBooleanFields(
                    RedisKeyPrefixEnum.DISCONNECTED, studyGroupId, alarmSetByUserId);
        } else {
            redisService.deleteLongBooleanField(RedisKeyPrefixEnum.DISCONNECTED, studyGroupId, userId);
        }
    }

    private void sendMessageToActiveSessions(Long studyGroupId, OutgoingGroupChatMessageDto outgoingMessageDto) throws IOException {
        Set<WebSocketSession> activeSessions = sessionsByStudyGroupId.get(studyGroupId);

        for (WebSocketSession activeSession : activeSessions) {
            sendMessage(activeSession, outgoingMessageDto);
        }
    }

    private void saveAndNotifyDisconnectedUsers(Long studyGroupId, OutgoingGroupChatMessageDto outgoingMessageDto,
                                                IncomingGroupChatMessageDto incomingMessageDto) {
        Map<Long, Boolean> alarmSetByReceiverId = redisService.EntriesLongBoolean(
                RedisKeyPrefixEnum.DISCONNECTED, studyGroupId);

        alarmSetByReceiverId.forEach((receiverId, isAlarm) -> {
            groupChatMessageService.asyncSave(receiverId, studyGroupId, outgoingMessageDto);

            String fcmToken = redisService.getValue(RedisKeyPrefixEnum.FCM, receiverId);
            if (isAlarm && fcmToken != null) {
                fcmService.sendGroupChatAlarm(incomingMessageDto, fcmToken);
            }
        });
    }

    private void sendMessage(WebSocketSession session, OutgoingGroupChatMessageDto outgoingMessageDto) throws IOException {
        String messageJson = objectMapper.writeValueAsString(outgoingMessageDto);
        TextMessage textMessage = new TextMessage(messageJson);
        session.sendMessage(textMessage);
    }
}