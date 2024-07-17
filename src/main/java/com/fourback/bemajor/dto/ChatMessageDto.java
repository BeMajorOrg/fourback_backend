package com.fourback.bemajor.dto;

import com.fourback.bemajor.domain.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageDto {
    private String message;
    private String sender;
    private LocalDateTime sendTime;

    public ChatMessage toMessageEntity(String oauth2Id){
        return ChatMessage.builder()
                .message(this.message)
                .sender(this.sender)
                .sendTime(this.sendTime)
                .oauth2Id(oauth2Id)
                .build();
    }
}
