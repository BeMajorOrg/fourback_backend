package com.fourback.bemajor.domain.chat.dto;

import com.fourback.bemajor.domain.chat.entitiy.GroupChatMessageEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto {
    private String content;
    private String senderName;
    private Long senderId;
    private LocalDateTime sendTime;

    public GroupChatMessageEntity toMessageEntity(Long userId, Long studyGroupId) {
        return GroupChatMessageEntity.builder()
                .message(this.content).senderId(senderId)
                .senderName(this.senderName).receiverId(userId)
                .studyGroupId(studyGroupId).sendTime(this.sendTime)
                .build();
    }
}
