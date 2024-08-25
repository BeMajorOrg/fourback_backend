package com.fourback.bemajor.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionBody {
    private int code;
    private String message;
}
