package com.fourback.bemajor.domain.studygroup.dto;

import com.fourback.bemajor.domain.studygroup.entity.GroupChatMessageEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OutgoingGroupChatMessageDto {
    private String content;
    private Long senderId;
    private LocalDateTime sendTime;

    public GroupChatMessageEntity toMessageEntity(Long receiverId, Long studyGroupId) {
        return GroupChatMessageEntity.builder()
                .senderId(senderId)
                .message(this.content)
                .receiverId(receiverId)
                .sendTime(this.sendTime)
                .studyGroupId(studyGroupId)
                .build();
    }
}
