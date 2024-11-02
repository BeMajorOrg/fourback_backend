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

    public static OutgoingGroupChatMessageDto from(GroupChatMessageEntity groupChatMessageEntity) {
        return OutgoingGroupChatMessageDto.builder()
                .content(groupChatMessageEntity.getMessage())
                .senderId(groupChatMessageEntity.getSenderId())
                .sendTime(groupChatMessageEntity.getSendTime())
                .build();
    }

    public static OutgoingGroupChatMessageDto of(Long senderId, LocalDateTime sendTime, String content) {
        return OutgoingGroupChatMessageDto.builder()
                .senderId(senderId)
                .sendTime(sendTime)
                .content(content)
                .build();
    }
}
