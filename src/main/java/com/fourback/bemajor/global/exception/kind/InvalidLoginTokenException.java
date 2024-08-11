package com.fourback.bemajor.global.exception.kind;

import org.springframework.http.HttpStatusCode;

public class InvalidLoginTokenException extends CustomException{
    public InvalidLoginTokenException(int code, String message, HttpStatusCode statusCode) {
        super(message);
        super.code = code;
        super.message = message;
        super.statusCode = statusCode;
    }
}
