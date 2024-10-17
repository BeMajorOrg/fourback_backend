package com.fourback.bemajor.domain.friendchat.entity;
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
@NoArgsConstructor
@Table(name = "friend_chat_message")
public class FriendChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(name = "message")
    private String message;

    @Column(name = "send_time")
    private LocalDateTime sendTime;

    @Column(name = "sender_name")
    private String senderName;

    public ChatMessageResponseDto toResponseDto() {
        return ChatMessageResponseDto.builder()
                .senderId(this.senderId)
                .content(this.message)
                .senderName(this.senderName)
                .sendTime(this.sendTime)
                .build();
    }
}
