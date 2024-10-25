package com.fourback.bemajor.domain.friendchat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendChatMessageResponseDto {
    private String content;
    private String senderName;
    private Long senderId;
    private LocalDateTime sendTime;


}
