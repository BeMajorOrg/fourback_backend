package com.fourback.bemajor.global.exception;

import com.fourback.bemajor.global.common.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException ex) {
        ExceptionDto body = ExceptionDto.of(ex.getCode(), ex.getMessage());

        return this.handleExceptionInternal(body, ex.getStatusCode());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException ex) {
        log.error(ex.getMessage(), ex);
        ExceptionDto body = ExceptionDto.of(4, "io error");

        return this.handleExceptionInternal(body, HttpStatus.LOCKED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        ExceptionDto body = ExceptionDto.of(7, "validation error");

        return this.handleExceptionInternal(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        ExceptionDto body = ExceptionDto.of(8, "runtime error");

        return this.handleExceptionInternal(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<?> handleExceptionInternal(ExceptionDto body, HttpStatusCode statusCode) {
        return ResponseUtil.onFailed(statusCode, body);
    }
}
