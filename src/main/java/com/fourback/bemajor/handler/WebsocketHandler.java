package com.fourback.bemajor.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourback.bemajor.domain.StudyGroup;
import com.fourback.bemajor.dto.MessageDto;
import com.fourback.bemajor.repository.StudyGroupRepository;
import com.fourback.bemajor.service.MessageService;
import com.fourback.bemajor.service.RedisService;
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
    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        long studyGroupId = Long.parseLong(Objects.requireNonNull(session.getUri()).getQuery().split("&")[0].split("=")[1]);
        String oauth2Id = Objects.requireNonNull(session.getUri()).getQuery().split("&")[1].split("=")[1];
        redisService.deleteDisConnectUser(Long.toString(studyGroupId), oauth2Id);
        websocketSessionsMap.get(studyGroupId).add(session);
        List<MessageDto> messageDtos = messageService.getMessages(oauth2Id);
        for (MessageDto messageDto : messageDtos) {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageDto)));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        long studyGroupId = Long.parseLong(Objects.requireNonNull(session.getUri()).getQuery().split("&")[0].split("=")[1]);
        String senderName = Objects.requireNonNull(session.getUri()).getQuery().split("&")[2].split("=")[1];
        String msg = message.getPayload();
        Set<WebSocketSession> sessions = websocketSessionsMap.get(studyGroupId);
        MessageDto messageDto = new MessageDto(msg, senderName, LocalDateTime.now());
        for (WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageDto)));
        }
        Map<String, String> disconnectedUser = redisService.getDisConnectUser(Long.toString(studyGroupId));
        disconnectedUser.forEach((oauth2Id, fcmToken) -> {
            messageService.saveMessageByOauth2Id(oauth2Id,messageDto);
            //fcm토큰으로 알림 전송하기
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        long studyGroupId = Long.parseLong(Objects.requireNonNull(session.getUri()).getQuery().split("&")[0].split("=")[1]);
        String oauth2Id = Objects.requireNonNull(session.getUri()).getQuery().split("&")[1].split("=")[1];
        websocketSessionsMap.get(studyGroupId).remove(session);
        //fcm 토큰 가져오기
        redisService.putDisConnectUser(Long.toString(studyGroupId), oauth2Id, "fcm토큰 넣어야함");
        messageService.deleteMessagesByOauth2Id(oauth2Id);
    }


    @PostConstruct
    public void init(){
        List<StudyGroup> studyGroups = studyGroupRepository.findAll();
        studyGroups.forEach(studyGroup -> {
            websocketSessionsMap.put(studyGroup.getId(), Collections.newSetFromMap(new ConcurrentHashMap<>()));
        });
    }
}