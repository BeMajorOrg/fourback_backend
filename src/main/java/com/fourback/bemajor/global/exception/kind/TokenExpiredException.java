package com.fourback.bemajor.global.exception.kind;

import com.fourback.bemajor.global.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class TokenExpiredException extends CustomException {
    public TokenExpiredException(String message) {
        super(0, message, HttpStatus.UNAUTHORIZED);
    }
}
