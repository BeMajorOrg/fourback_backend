package com.fourback.bemajor.global.exception.kind;

import com.fourback.bemajor.global.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class NotFoundException extends CustomException {
    public NotFoundException(String message) {
        super(2, message, HttpStatus.NOT_FOUND);
    }
}
