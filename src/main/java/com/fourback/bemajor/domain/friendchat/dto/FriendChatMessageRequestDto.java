package com.fourback.bemajor.domain.friendchat.dto;

import com.fourback.bemajor.domain.chat.dto.ChatMessageResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FriendChatMessageRequestDto {
    private String content;
    private String senderName;

    public FriendChatMessageResponseDto toResponseDto(Long userId) {
        return FriendChatMessageResponseDto.builder()
                .content(this.content)
                .senderId(userId)
                .senderName(this.senderName)
                .build();
    }
}
