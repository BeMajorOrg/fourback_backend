package com.fourback.bemajor.global.exception.kind;

import com.fourback.bemajor.global.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class InvalidTokenException extends CustomException {
    public InvalidTokenException(String message) {
        super(1, message, HttpStatus.UNAUTHORIZED);
    }
}
