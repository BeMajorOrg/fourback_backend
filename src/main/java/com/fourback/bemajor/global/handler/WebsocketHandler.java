package com.fourback.bemajor.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourback.bemajor.domain.studygroup.entity.StudyGroup;
import com.fourback.bemajor.domain.studygroup.repository.StudyGroupRepository;
import com.fourback.bemajor.dto.ChatMessageDto;
import com.fourback.bemajor.global.common.service.RedisService;
import com.fourback.bemajor.service.FcmService;
import com.fourback.bemajor.service.ChatMessageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
public class WebsocketHandler extends TextWebSocketHandler {
    private final StudyGroupRepository studyGroupRepository;
    private final Map<Long, Set<WebSocketSession>> websocketSessionsMap;
    private final RedisService redisService;
    private final ChatMessageService chatMessageService;
    private final ObjectMapper objectMapper;
    private final FcmService fcmService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        String[] querys = Objects.requireNonNull(session.getUri()).getQuery().split("&");
        long studyGroupId = Long.parseLong(querys[0].split("=")[1]);
        Long oauth2Id = querys[1].split("=")[1];
        redisService.deleteDisConnectUser(Long.toString(studyGroupId), oauth2Id);
        websocketSessionsMap.get(studyGroupId).add(session);
        List<ChatMessageDto> chatMessageDtos = chatMessageService.getMessages(oauth2Id, studyGroupId);
        for (ChatMessageDto chatMessageDto : chatMessageDtos) {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessageDto)));
        }
        chatMessageService.deleteMessages(oauth2Id, studyGroupId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String[] querys = Objects.requireNonNull(session.getUri()).getQuery().split("&");
        long studyGroupId = Long.parseLong(querys[0].split("=")[1]);
        String senderName = querys[2].split("=")[1];
        String studyGroupName = querys[3].split("=")[1];
        String msg = message.getPayload();
        Set<WebSocketSession> sessions = websocketSessionsMap.get(studyGroupId);
        ChatMessageDto chatMessageDto = new ChatMessageDto(msg, senderName, LocalDateTime.now());
        for (WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessageDto)));
        }
        Map<String, String> disconnectedUser = redisService.getDisConnectUser(Long.toString(studyGroupId));
        disconnectedUser.forEach((oauth2Id, fcmToken) -> {
            chatMessageService.saveMessage(oauth2Id, chatMessageDto, studyGroupId);
            fcmService.sendalarm(chatMessageDto, oauth2Id, studyGroupName);
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String[] querys = Objects.requireNonNull(session.getUri()).getQuery().split("&");
        long studyGroupId = Long.parseLong(querys[0].split("=")[1]);
        String oauth2Id = querys[1].split("=")[1];
        websocketSessionsMap.get(studyGroupId).remove(session);
        String fcmToken = redisService.getFcmToken(oauth2Id);
        redisService.putDisConnectUser(Long.toString(studyGroupId), oauth2Id, fcmToken);
        chatMessageService.deleteMessages(oauth2Id,studyGroupId);
    }


    @PostConstruct
    public void init(){
        List<StudyGroup> studyGroups = studyGroupRepository.findAll();
        studyGroups.forEach(studyGroup -> {
            websocketSessionsMap.put(studyGroup.getId(), Collections.newSetFromMap(new ConcurrentHashMap<>()));
        });
    }
}