package com.fourback.bemajor.domain.chat.entitiy;

import com.fourback.bemajor.domain.chat.dto.ChatMessageResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "group_chat_message")
public class GroupChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_chat_message_id")
    Long id;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(name = "study_group_id")
    private Long studyGroupId;

    @Column(name = "content")
    private String message;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "send_time")
    private LocalDateTime sendTime;

    public ChatMessageResponseDto toMessageResponseDto() {
        return ChatMessageResponseDto.builder()
                .content(this.message)
                .senderId(this.senderId)
                .senderName(this.senderName)
                .sendTime(this.sendTime)
                .build();
    }
}
