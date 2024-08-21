package com.fourback.bemajor.domain.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourback.bemajor.domain.chat.dto.ChatMessageDto;
import com.fourback.bemajor.domain.chat.service.GroupChatMessageService;
import com.fourback.bemajor.domain.studygroup.entity.StudyGroup;
import com.fourback.bemajor.domain.studygroup.repository.StudyGroupRepository;
import com.fourback.bemajor.domain.studygroup.repository.StudyJoinedRepository;
import com.fourback.bemajor.global.common.service.FcmService;
import com.fourback.bemajor.global.common.service.RedisService;
import com.fourback.bemajor.global.security.JWTUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class GroupChatHandler extends TextWebSocketHandler {
    private final StudyGroupRepository studyGroupRepository;
    private final Map<Long, Set<WebSocketSession>> studyGrupIdSessionsMap;
    private final Map<WebSocketSession, Pair<Long, Long>> sessionIdsMap;
    private final RedisService redisService;
    private final GroupChatMessageService groupChatMessageService;
    private final ObjectMapper objectMapper;
    private final FcmService fcmService;
    private final StudyJoinedRepository studyJoinedRepository;
    private final JWTUtil jwtUtil;

    @Override
    @Transactional
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        String accessToken = session.getHandshakeHeaders().getFirst("access");
        Long userId = jwtUtil.getUserId(accessToken);
        Long studyGroupId = Long.valueOf(Objects.requireNonNull(session.getUri())
                .getQuery().split("&")[0].split("=")[1]);
        sessionIdsMap.put(session, Pair.of(userId, studyGroupId));
        this.putDisConnectUserFromDB(studyGroupId, userId);
        redisService.deleteDisConnectUser(studyGroupId, userId);
        studyGrupIdSessionsMap.get(studyGroupId).add(session);
        List<ChatMessageDto> chatMessageDtos =
                groupChatMessageService.getMessages(userId, studyGroupId);
        for (ChatMessageDto chatMessageDto : chatMessageDtos) {
            session.sendMessage(new TextMessage(
                    objectMapper.writeValueAsString(chatMessageDto)));
        }
        if (!chatMessageDtos.isEmpty())
            groupChatMessageService.deleteMessages(userId, studyGroupId);
    }

    @Override
    @Async("threadPoolTaskExecutor")
    @Transactional
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws Exception {
        String[] querys = Objects.requireNonNull(session.getUri())
                .getQuery().split("&");
        String senderName = querys[1].split("=")[1];
        String studyGroupName = querys[2].split("=")[1];
        String msg = message.getPayload();
        Pair<Long, Long> ids = sessionIdsMap.get(session);
        Long senderId = ids.getLeft();
        Long studyGroupId = ids.getRight();
        Set<WebSocketSession> onSessions = studyGrupIdSessionsMap.get(studyGroupId);
        ChatMessageDto chatMessageDto =
                new ChatMessageDto(msg, senderName, senderId);
        for (WebSocketSession onSession : onSessions) {
            onSession.sendMessage(new TextMessage(
                    objectMapper.writeValueAsString(chatMessageDto)));
        }
        Set<Long> disconnectedUserId = redisService.getDisConnectUser(studyGroupId);
        disconnectedUserId.forEach(userId -> {
            String fcmToken = redisService.getFcmToken(userId);
            if(fcmToken==null)
                return;
            groupChatMessageService.saveMessage(userId, chatMessageDto, studyGroupId);
            fcmService.sendalarm(chatMessageDto, fcmToken, studyGroupName);
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Pair<Long, Long> ids = sessionIdsMap.get(session);
        Long userId = ids.getLeft();
        Long studyGroupId = ids.getRight();
        redisService.putDisConnectUser(studyGroupId, userId);
        studyGrupIdSessionsMap.get(studyGroupId).remove(session);
        sessionIdsMap.remove(session);
    }

    private void putDisConnectUserFromDB(Long studyGroupId, Long userId) {
        if (!redisService.checkDisConnectUserKey(studyGroupId)) {
            redisService.putDisConnectUserAll(studyGroupId,
                    studyJoinedRepository.findByStudyGroupIdNotUserId(studyGroupId, userId));
        }
    }

    @PostConstruct
    public void setupStudyGroupChatRoom() {
        List<StudyGroup> studyGroups = studyGroupRepository.findAll();
        studyGroups.forEach(studyGroup -> {
            studyGrupIdSessionsMap.put(studyGroup.getId(),
                    Collections.newSetFromMap(new ConcurrentHashMap<>()));
        });
    }
}