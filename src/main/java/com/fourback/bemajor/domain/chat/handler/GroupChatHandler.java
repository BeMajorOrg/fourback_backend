package com.fourback.bemajor.domain.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourback.bemajor.domain.chat.dto.ChatMessageRequestDto;
import com.fourback.bemajor.domain.chat.dto.ChatMessageResponseDto;
import com.fourback.bemajor.domain.chat.service.GroupChatMessageService;
import com.fourback.bemajor.domain.studygroup.entity.StudyGroup;
import com.fourback.bemajor.domain.studyGroupNotification.repository.StudyGroupNotificationRepository;
import com.fourback.bemajor.domain.studygroup.repository.StudyGroupRepository;
import com.fourback.bemajor.domain.studygroup.repository.StudyJoinedRepository;
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

@Component
@RequiredArgsConstructor
public class GroupChatHandler extends TextWebSocketHandler {
    private final StudyGroupRepository studyGroupRepository;
    private final Map<Long, Set<WebSocketSession>> studyGrupIdSessionsMap;
    private final Map<WebSocketSession, Pair<Long, Long>> sessionIdsMap = new ConcurrentHashMap<>();
    private final RedisService redisService;
    private final GroupChatMessageService groupChatMessageService;
    private final ObjectMapper objectMapper;
    private final FcmService fcmService;
    private final StudyGroupNotificationRepository studyGroupNotificationRepository;

    @Override
    @Transactional
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        Long userId = (Long) session.getAttributes().get("userId");
        Long studyGroupId = Long.valueOf(Objects.requireNonNull(session.getUri())
                .getQuery().split("&")[0].split("=")[1]);
        sessionIdsMap.put(session, Pair.of(userId, studyGroupId));
        studyGrupIdSessionsMap.get(studyGroupId).add(session);
        this.putDisConnectUserFromDB(studyGroupId, userId);
        redisService.removeLongMember(RedisKeyPrefixEnum.DISCONNECTED, studyGroupId, userId);
        List<ChatMessageResponseDto> chatMessageResponseDtos =
                groupChatMessageService.getMessages(userId, studyGroupId);
        for (ChatMessageResponseDto chatMessageResponseDto : chatMessageResponseDtos) {
            session.sendMessage(new TextMessage(
                    objectMapper.writeValueAsString(chatMessageResponseDto)));
        }
        if (!chatMessageResponseDtos.isEmpty())
            groupChatMessageService.deleteMessages(userId, studyGroupId);
    }

    @Override
    @Transactional
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws Exception {
        String payload = message.getPayload();
        Pair<Long, Long> ids = sessionIdsMap.get(session);
        Long senderId = ids.getLeft();
        Long studyGroupId = ids.getRight();
        Set<WebSocketSession> onSessions = studyGrupIdSessionsMap.get(studyGroupId);

        ChatMessageRequestDto chatMessageRequestDto =
                objectMapper.readValue(payload, ChatMessageRequestDto.class);
        ChatMessageResponseDto chatMessageResponseDto =
                chatMessageRequestDto.toResponseDto(senderId);

        LocalDateTime currentServerTime = LocalDateTime.now();
        chatMessageResponseDto.setSendTime(currentServerTime);

        for (WebSocketSession onSession : onSessions) {
            onSession.sendMessage(new TextMessage(
                    objectMapper.writeValueAsString(chatMessageResponseDto)));
        }
        Set<Long> disconnectedUserId = redisService.getLongMembers(
                RedisKeyPrefixEnum.DISCONNECTED, studyGroupId);
        disconnectedUserId.forEach(userId -> {
            String fcmToken = redisService.getValue(RedisKeyPrefixEnum.FCM, userId);
            if (fcmToken == null)
                return;
            groupChatMessageService.saveMessage(userId, chatMessageResponseDto, studyGroupId);
            fcmService.sendalarm(chatMessageResponseDto, fcmToken,
                    chatMessageRequestDto.getStudyGroupName());
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Pair<Long, Long> ids = sessionIdsMap.get(session);
        Long userId = ids.getLeft();
        Long studyGroupId = ids.getRight();
        redisService.addLongMember(RedisKeyPrefixEnum.DISCONNECTED, studyGroupId, userId);
        studyGrupIdSessionsMap.get(studyGroupId).remove(session);
        sessionIdsMap.remove(session);
    }

    private void putDisConnectUserFromDB(Long studyGroupId, Long userId) {
        if (!redisService.checkKey(RedisKeyPrefixEnum.DISCONNECTED, studyGroupId)) {
            redisService.addLongMembers(RedisKeyPrefixEnum.DISCONNECTED, studyGroupId,
                    studyGroupNotificationRepository.findByStudyGroupIdNotUserId(
                            studyGroupId, userId));
        }
    }

    @PostConstruct
    protected void setupStudyGroupChatRoom() {
        List<StudyGroup> studyGroups = studyGroupRepository.findAll();
        studyGroups.forEach(studyGroup -> {
            studyGrupIdSessionsMap.put(studyGroup.getId(),
                    Collections.newSetFromMap(new ConcurrentHashMap<>()));
        });
    }
}