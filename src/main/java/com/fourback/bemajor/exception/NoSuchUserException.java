package com.fourback.bemajor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class NoSuchUserException extends CustomException{
    public NoSuchUserException(int code, String message, HttpStatusCode httpStatusCode) {
        super(message);
        super.code = code;
        super.message = message;
        super.statusCode = statusCode;
    }
}
