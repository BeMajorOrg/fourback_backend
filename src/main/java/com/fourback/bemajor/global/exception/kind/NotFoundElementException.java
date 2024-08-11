package com.fourback.bemajor.global.exception.kind;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class NotFoundElementException extends CustomException{
    public NotFoundElementException(int code, String message, HttpStatusCode statusCode) {
        super(message);
        super.code = code;
        super.message = message;
        super.statusCode = statusCode;
    }
}
