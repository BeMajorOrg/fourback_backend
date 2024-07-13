package com.fourback.bemajor.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourback.bemajor.dto.MessageDto;
import com.fourback.bemajor.service.MessageService;
import com.fourback.bemajor.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class WebsocketHandler extends TextWebSocketHandler {
    private final Map<Long, Set<WebSocketSession>> websocketSessionsMap;
    private final RedisService redisService;
    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        long studyGroupId = Long.parseLong(Objects.requireNonNull(session.getUri()).getQuery().split("&")[0].split("=")[1]);
        String oauth2Id = Objects.requireNonNull(session.getUri()).getQuery().split("&")[1].split("=")[1];
        //studygroup 만들 때 websocketSessionsMap을 주입받아 새로운 set 넣어주기.
        /*if (!websocketSessionsMap.containsKey(StudyGroupId)) {
            websocketSessionsMap.put(StudyGroupId, Collections.newSetFromMap(new ConcurrentHashMap<>()));
        }*/
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
        //senderOauth2Id 대신 이름 넣기
        String senderOauth2Id = Objects.requireNonNull(session.getUri()).getQuery().split("&")[1].split("=")[1];
        String msg = message.getPayload();
        Set<WebSocketSession> sessions = websocketSessionsMap.get(studyGroupId);
        MessageDto messageDto = new MessageDto(msg, senderOauth2Id, LocalDateTime.now());
        for (WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageDto)));
        }
        /*
        fcm을 통해 oauth2Id와 fcm token 가져오고 이를 통해 서버에 메세지 저장하며 fcm으로 알림 전송
         */
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

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}