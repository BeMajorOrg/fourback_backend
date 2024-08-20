package com.fourback.bemajor.domain.chat.entitiy;

import com.fourback.bemajor.domain.chat.dto.ChatMessageDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "message")
    private String message;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "sender_name")
    private String senderName;

    public ChatMessageDto toMessageDto() {
        return ChatMessageDto.builder()
                .message(this.message)
                .senderId(this.senderId)
                .senderName(this.senderName)
                .build();
    }
}
