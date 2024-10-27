package com.fourback.bemajor.global.exception.kind;

import com.fourback.bemajor.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {
    public NotFoundException(String message) {
        super(2, message, HttpStatus.NOT_FOUND);
    }
}
