package com.fourback.bemajor.global.common.enums;

import lombok.Getter;

@Getter
public enum RedisKeyPrefixEnum {
    REFRESH("refresh:"),
    DISCONNECTED("disConnected:"),
    FCM("fcm:");

    private final String description;

    RedisKeyPrefixEnum(String description) {
        this.description = description;
    }
}
