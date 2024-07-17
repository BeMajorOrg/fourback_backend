package com.fourback.bemajor.domain;

import com.fourback.bemajor.dto.ChatMessageDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String oauth2Id;
    private Long StudyGroupId;
    private String message;
    private LocalDateTime sendTime;
    private String sender;

    public ChatMessageDto toMessageDto(){
        return new ChatMessageDto(this.message,this.sender,this.sendTime);
    }
}
