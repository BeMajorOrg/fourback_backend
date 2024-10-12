package com.fourback.bemajor.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
