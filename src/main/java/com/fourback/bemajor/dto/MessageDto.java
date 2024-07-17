package com.fourback.bemajor.dto;

import com.fourback.bemajor.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MessageDto {
    private String message;
    private String sender;
    private LocalDateTime sendTime;

    public Message toMessageEntity(String oauth2Id){
        return Message.builder()
                .message(this.message)
                .sender(this.sender)
                .sendTime(this.sendTime)
                .oauth2Id(oauth2Id)
                .build();
    }
}
