package com.fourback.bemajor.domain;

import com.fourback.bemajor.dto.MessageDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String oauth2Id;
    private String message;
    private LocalDateTime sendTime;
    private String sender;

    public MessageDto toMessageDto(){
        return new MessageDto(this.message,this.sender,this.sendTime);
    }
}
