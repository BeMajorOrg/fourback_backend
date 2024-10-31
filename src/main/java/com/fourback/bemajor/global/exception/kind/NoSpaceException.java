package com.fourback.bemajor.global.exception.kind;

import com.fourback.bemajor.global.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class NoSpaceException extends CustomException {
    public NoSpaceException(String message) {
        super(5, message, HttpStatus.BAD_REQUEST);
    }
}
