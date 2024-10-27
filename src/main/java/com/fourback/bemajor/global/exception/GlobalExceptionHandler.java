package com.fourback.bemajor.global.exception;

import com.fourback.bemajor.global.common.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionDto> handleCustomException(CustomException ex) {
        ExceptionDto body = ExceptionDto.of(ex.getCode(), ex.getMessage());

        return this.handleExceptionInternal(body, ex.getStatusCode());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ExceptionDto> handleIOException(IOException ex) {
        ExceptionDto body = ExceptionDto.of(4, ex.getMessage());

        return this.handleExceptionInternal(body, HttpStatus.LOCKED);
    }

    private ResponseEntity<ExceptionDto> handleExceptionInternal(ExceptionDto body,
                                                                 HttpStatusCode statusCode) {
        return ResponseUtil.onFailed(statusCode, body);
    }
}
