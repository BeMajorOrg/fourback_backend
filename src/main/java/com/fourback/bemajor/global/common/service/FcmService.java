package com.fourback.bemajor.global.common.service;

import com.fourback.bemajor.domain.chat.dto.ChatMessageDto;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FcmService {
    @Value("${fcm.key}")
    private String googleCredentials;

    public void sendalarm(ChatMessageDto chatMessageDto,
                          String fcmToken, String StudyGroupName) {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(StudyGroupName)
                        .setBody(chatMessageDto.getSenderName() + ":"
                                + chatMessageDto.getMessage())
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
