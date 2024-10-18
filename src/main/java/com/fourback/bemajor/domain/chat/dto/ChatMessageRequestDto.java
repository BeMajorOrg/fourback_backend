package com.fourback.bemajor.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageRequestDto {
    private String content;
    private String senderName;
    private String studyGroupName;

    public ChatMessageResponseDto toResponseDto(Long userId) {
        return ChatMessageResponseDto.builder()
                .content(this.content)
                .senderId(userId)
                .senderName(this.senderName)
                .build();
    }
}
