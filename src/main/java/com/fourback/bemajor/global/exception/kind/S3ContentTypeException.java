package com.fourback.bemajor.global.exception.kind;

import com.fourback.bemajor.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class S3ContentTypeException extends CustomException {
    public S3ContentTypeException(String message) {
        super(8, message, HttpStatus.BAD_REQUEST);
    }
}
