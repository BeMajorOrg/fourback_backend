package com.fourback.bemajor.domain.chat.dto;

import com.fourback.bemajor.domain.chat.entitiy.GroupChatMessageEntity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private String message;
    private String senderName;
    private Long senderId;

    public GroupChatMessageEntity toMessageEntity(Long userId, Long studyGroupId) {
        return GroupChatMessageEntity.builder()
                .message(this.message).senderId(senderId)
                .senderName(this.senderName).receiverId(userId)
                .studyGroupId(studyGroupId)
                .build();
    }
}
