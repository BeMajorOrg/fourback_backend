package com.fourback.bemajor.global.exception.kind;

import com.fourback.bemajor.global.exception.CustomException;
import org.springframework.http.HttpStatus;

/**
 * 업로드 파일 이름 부적절 예외
 */
public class FilenameInvalidException extends CustomException {

    public FilenameInvalidException(String message) {
        super(400, message, HttpStatus.BAD_REQUEST);
    }
}
