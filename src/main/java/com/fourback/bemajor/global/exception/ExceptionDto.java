package com.fourback.bemajor.global.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExceptionDto {
    private int code;
    private String message;

    public static ExceptionDto of(int code, String message) {
        return ExceptionDto.builder()
                .code(code)
                .message(message)
                .build();
    }
}