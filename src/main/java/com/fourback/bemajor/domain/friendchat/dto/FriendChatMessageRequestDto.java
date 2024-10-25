package com.fourback.bemajor.domain.friendchat.dto;

import lombok.Getter;
import lombok.Setter;

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
