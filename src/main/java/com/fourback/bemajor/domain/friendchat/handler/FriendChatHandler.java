package com.fourback.bemajor.domain.friendchat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourback.bemajor.domain.friendchat.dto.FriendChatMessageRequestDto;
import com.fourback.bemajor.domain.friendchat.dto.FriendChatMessageResponseDto;
import com.fourback.bemajor.domain.friendchat.service.FriendChatMessageService;
import com.fourback.bemajor.global.common.enums.FieldKeyEnum;
import com.fourback.bemajor.global.common.enums.KeyPrefixEnum;
import com.fourback.bemajor.global.common.service.FcmService;
import com.fourback.bemajor.global.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Component
@RequiredArgsConstructor
public class FriendChatHandler extends TextWebSocketHandler {


    private final ObjectMapper objectMapper;
    private final FriendChatMessageService friendChatMessageService;
    private final RedisService redisService;
    private final FcmService fcmService;
    private final Map<Long, Set<WebSocketSession>> chatRoomSessionsMap;
    private final Map<WebSocketSession, Pair<Long, Long>> sessionIdsMap = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        String chatRoomId = session.getUri().getQuery().split("=")[1];
        String[] ids = chatRoomId.split("_");
        Long userId1 = Long.parseLong(ids[0]);
        Long userId2 = Long.parseLong(ids[1]);
        Long otherUserId = userId.equals(userId1) ? userId2 : userId1;
        Long chatRoomIdLong  = Math.min(userId1, userId2) * 1_000_000 + Math.max(userId1, userId2);
        sessionIdsMap.put(session, Pair.of(userId, chatRoomIdLong));
        chatRoomSessionsMap.computeIfAbsent(chatRoomIdLong , k -> new HashSet<>()).add(session);
        List<FriendChatMessageResponseDto> storedMessages = friendChatMessageService.getMessages(otherUserId,userId);
        if (!storedMessages.isEmpty()) {
            try {
                for (FriendChatMessageResponseDto message : storedMessages) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
                }
                friendChatMessageService.deleteMessages(otherUserId, userId);
            } catch (Exception e) {
                System.err.println("메시지 전송 중 오류 발생: " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Pair<Long, Long> ids = sessionIdsMap.get(session);
        Long senderId = ids.getLeft();
        Long chatRoomIdLong = ids.getRight();

        FriendChatMessageRequestDto friendchatMessageRequestDto =
                objectMapper.readValue(payload, FriendChatMessageRequestDto.class);
        FriendChatMessageResponseDto friendchatMessageResponseDto =
                friendchatMessageRequestDto.toResponseDto(senderId);

        LocalDateTime currentServerTime = LocalDateTime.now();
        friendchatMessageResponseDto.setSendTime(currentServerTime);

        Set<WebSocketSession> onSessions = chatRoomSessionsMap.get(chatRoomIdLong);

        for (WebSocketSession onSession : onSessions) {
            onSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(friendchatMessageResponseDto)));
        }

        Long userId1 = chatRoomIdLong / 1_000_000;
        Long userId2 = chatRoomIdLong % 1_000_000;
        Long receiverId = userId1.equals(senderId) ? userId2 : userId1;
        boolean isReceiverOnline = isUserOnline(receiverId, onSessions);

        if (!isReceiverOnline) {
            friendChatMessageService.saveMessage(receiverId, friendchatMessageResponseDto);
            String fcmToken = redisService.getFieldValue(
                KeyPrefixEnum.TOKENS.getKeyPrefix() + receiverId, FieldKeyEnum.FCM.getFieldKey());
            if (fcmToken != null) {
                fcmService.sendFriendChatAlarm(friendchatMessageResponseDto, fcmToken);
            }
        }

//        if (onSessions != null && onSessions.size() == 1) {
//            friendChatMessageService.saveMessage(receiverId, friendchatMessageResponseDto);
//            String fcmToken = redisService.getValue(KeyPrefixEnum.FCM, receiverId);
//            if (fcmToken != null) {
//                fcmService.sendFriendChatAlarm(friendchatMessageResponseDto, fcmToken);
//            }
//        } else {
//            for (WebSocketSession onSession : onSessions) {
//                onSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(friendchatMessageResponseDto)));
//            }
//        }


    }

    private boolean isUserOnline(Long receiverId, Set<WebSocketSession> onSessions) {
        for (WebSocketSession onSession : onSessions) {
            Pair<Long, Long> sessionPair = sessionIdsMap.get(onSession);
            if (sessionPair != null && sessionPair.getLeft().equals(receiverId)) {
                return true; // 상대방이 채팅방에 접속해 있음
            }
        }
        return false; // 상대방이 접속해 있지 않음
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Pair<Long, Long> ids = sessionIdsMap.get(session);
        Long chatRoomId = ids.getRight();

        chatRoomSessionsMap.get(chatRoomId).remove(session);
        if (chatRoomSessionsMap.get(chatRoomId).isEmpty()) {
            chatRoomSessionsMap.remove(chatRoomId);
        }


        sessionIdsMap.remove(session);

    }
}