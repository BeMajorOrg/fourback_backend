package com.fourback.bemajor.global.common.enums;

import lombok.Getter;

@Getter
public enum FieldKeyEnum {
    FCM("fcm"),
    REFRESH("refresh");

    private final String fieldKey;

    FieldKeyEnum(String fieldKey) {
        this.fieldKey = fieldKey;
    }
}
