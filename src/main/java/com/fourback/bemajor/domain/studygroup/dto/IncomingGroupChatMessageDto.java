package com.fourback.bemajor.domain.studygroup.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class IncomingGroupChatMessageDto {
    private String content;
    private String senderName;
    private String studyGroupName;

    public OutgoingGroupChatMessageDto toOutgoingMessageDto(Long userId, LocalDateTime sendTime) {
        return OutgoingGroupChatMessageDto.builder()
                .senderId(userId)
                .sendTime(sendTime)
                .content(this.content)
                .build();
    }
}
