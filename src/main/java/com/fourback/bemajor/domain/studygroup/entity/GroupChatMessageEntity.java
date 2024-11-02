package com.fourback.bemajor.domain.studygroup.entity;

import com.fourback.bemajor.domain.studygroup.dto.OutgoingGroupChatMessageDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "group_chat_message")
public class GroupChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_chat_message_id")
    private Long id;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(name = "study_group_id")
    private Long studyGroupId;

    @Column(name = "content")
    private String message;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "send_time")
    private LocalDateTime sendTime;

    public static GroupChatMessageEntity of(Long receiverId, Long studyGroupId,
                                            OutgoingGroupChatMessageDto outgoingMessageDto) {
        return GroupChatMessageEntity.builder()
                .senderId(outgoingMessageDto.getSenderId())
                .message(outgoingMessageDto.getContent())
                .receiverId(receiverId)
                .sendTime(outgoingMessageDto.getSendTime())
                .studyGroupId(studyGroupId)
                .build();
    }
}
