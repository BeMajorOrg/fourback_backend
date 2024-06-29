package com.fourback.bemajor.handler;

import com.fourback.bemajor.dto.ExceptionResponse;
import com.fourback.bemajor.exception.InvalidLoginTokenException;
import com.fourback.bemajor.exception.NoSuchStudyGroupException;
import com.fourback.bemajor.exception.NoSuchUserException;
import com.fourback.bemajor.exception.NotFoundElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundElementException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundElementException(NotFoundElementException ex) {
        ExceptionResponse body = new ExceptionResponse(ex.getCode(), ex.getMessage());
        return this.handleExceptionInternal(body, ex.getStatusCode());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundElementException(IOException ex) {
        ExceptionResponse body = new ExceptionResponse(2, ex.getMessage());
        return this.handleExceptionInternal(body, HttpStatus.LOCKED);
    }

    @ExceptionHandler(InvalidLoginTokenException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidLoginTokenException(InvalidLoginTokenException ex) {
        ExceptionResponse body = new ExceptionResponse(ex.getCode(), ex.getMessage());
        return this.handleExceptionInternal(body, ex.getStatusCode());
    }

    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchUserException(NoSuchUserException ex){
        ExceptionResponse body = new ExceptionResponse(ex.getCode(),ex.getMessage());
        return this.handleExceptionInternal(body,ex.getStatusCode());
    }

    @ExceptionHandler(NoSuchStudyGroupException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchStudyGroupException(NoSuchStudyGroupException ex){
        ExceptionResponse body = new ExceptionResponse(ex.getCode(),ex.getMessage());
        return this.handleExceptionInternal(body,ex.getStatusCode());
    }

    private ResponseEntity<ExceptionResponse> handleExceptionInternal(ExceptionResponse body, HttpStatusCode statusCode) {
        return ResponseEntity.status(statusCode)
                .body(body);
    }
}
