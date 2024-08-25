package com.fourback.bemajor.global.handler;

import com.fourback.bemajor.global.common.response.Response;
import com.fourback.bemajor.global.exception.ExceptionBody;
import com.fourback.bemajor.global.exception.kind.*;
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
    public ResponseEntity<ExceptionBody> handleNotFoundElementException(NotFoundElementException ex) {
        ExceptionBody body = new ExceptionBody(ex.getCode(), ex.getMessage());
        return this.handleExceptionInternal(body, ex.getStatusCode());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ExceptionBody> handleIOException(IOException ex) {
        ExceptionBody body = new ExceptionBody(2, ex.getMessage());
        return this.handleExceptionInternal(body, HttpStatus.LOCKED);
    }

    @ExceptionHandler(InvalidLoginTokenException.class)
    public ResponseEntity<ExceptionBody> handleInvalidLoginTokenException(InvalidLoginTokenException ex) {
        ExceptionBody body = new ExceptionBody(ex.getCode(), ex.getMessage());
        return this.handleExceptionInternal(body, ex.getStatusCode());
    }

    @ExceptionHandler(AccessTokenExpiredException.class)
    public ResponseEntity<ExceptionBody> handleAccessTokenExpiredException(AccessTokenExpiredException ex) {
        ExceptionBody body = new ExceptionBody(ex.getCode(), ex.getMessage());
        return this.handleExceptionInternal(body, ex.getStatusCode());
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ExceptionBody> handleNotAuthorizedException(NotAuthorizedException ex) {
        ExceptionBody body = new ExceptionBody(ex.getCode(), ex.getMessage());
        return this.handleExceptionInternal(body, ex.getStatusCode());
    }

    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity<ExceptionBody> handleNoSuchUserException(NoSuchUserException ex) {
        ExceptionBody body = new ExceptionBody(ex.getCode(), ex.getMessage());
        return this.handleExceptionInternal(body, ex.getStatusCode());
    }

    @ExceptionHandler(NoSuchStudyGroupException.class)
    public ResponseEntity<ExceptionBody> handleNoSuchStudyGroupException(NoSuchStudyGroupException ex) {
        ExceptionBody body = new ExceptionBody(ex.getCode(), ex.getMessage());
        return this.handleExceptionInternal(body, ex.getStatusCode());
    }

    private ResponseEntity<ExceptionBody> handleExceptionInternal(ExceptionBody body,
                                                                  HttpStatusCode statusCode) {
        return Response.onFailed(statusCode, body);
    }
}
