package com.fourback.bemajor.domain.friendchat.dto;

import com.fourback.bemajor.domain.chat.entitiy.GroupChatMessageEntity;
import com.fourback.bemajor.domain.friendchat.entity.FriendChatMessageEntity;
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
