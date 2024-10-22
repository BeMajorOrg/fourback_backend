package com.fourback.bemajor.global.common.service;

import com.fourback.bemajor.domain.chat.dto.ChatMessageResponseDto;
import com.fourback.bemajor.domain.friendchat.dto.FriendChatMessageResponseDto;
import com.fourback.bemajor.domain.studygroup.dto.request.StudyGroupAlarmDto;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FcmService {
    @Value("${fcm.key}")
    private String googleCredentials;

    @Async("threadPoolTaskExecutor")
    public void sendalarm(ChatMessageResponseDto chatMessageDto,
                          String fcmToken, String StudyGroupName) {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(StudyGroupName)
                        .setBody(chatMessageDto.getSenderName() + ":"
                                + chatMessageDto.getContent())
                        .build())
                .putData("senderId", chatMessageDto.getSenderId().toString())
                .setToken(fcmToken)
                .build();
        FirebaseMessaging.getInstance().sendAsync(message);
    }

    /**
     * 스터디 그룹 초대,입장 등 알람 보내기
     * @param studyGroupAlarmDto
     */
    @Async("threadPoolTaskExecutor")
    public void sendStudyGroupAlarm(StudyGroupAlarmDto studyGroupAlarmDto) {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(studyGroupAlarmDto.getTitle())
                        .setBody(studyGroupAlarmDto.getMessage())
                        .build())
                .setToken(studyGroupAlarmDto.getFcmToken())
                .build();
        FirebaseMessaging.getInstance().sendAsync(message);
    }

    @Async("threadPoolTaskExecutor")
    public void sendFriendChatAlarm(FriendChatMessageResponseDto chatMessageDto,
                                    String fcmToken) {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(chatMessageDto.getSenderName())
                        .setBody(chatMessageDto.getContent())
                        .build())
                .putData("senderId", chatMessageDto.getSenderId().toString())
                .setToken(fcmToken)
                .build();
        FirebaseMessaging.getInstance().sendAsync(message);
    }

    @PostConstruct
    public void init() throws IOException {
        ClassPathResource resource = new ClassPathResource(googleCredentials);
        try (InputStream in = resource.getInputStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(in))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        }
    }
}
