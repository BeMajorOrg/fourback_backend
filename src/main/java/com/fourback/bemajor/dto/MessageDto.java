package com.fourback.bemajor.dto;

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
}
