package com.fourback.bemajor.global.exception.kind;

import com.fourback.bemajor.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UnableToDeleteException extends CustomException {
    public UnableToDeleteException(String message) {
        super(6, message, HttpStatus.LOCKED);
    }
}
