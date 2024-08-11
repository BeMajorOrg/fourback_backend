package com.fourback.bemajor.domain.global.exception.kind;

import org.springframework.http.HttpStatus;

/**
 * 업로드 파일 이름 부적절 예외
 */
public class FilenameInvalidException extends CustomException {

  public FilenameInvalidException(String message) {
    super(message);
    code = 400;
    statusCode = HttpStatus.BAD_REQUEST;
  }
}
