package com.fourback.bemajor.global.common.enums;

import lombok.Getter;

@Getter
public enum RedisKeyPrefixEnum {
    FCM("fcm:"),
    REFRESH("refresh:"),
    DISCONNECTED("disConnected:"),
    LOGOUT_ACCESS("logoutAccess:");

    private final String keyPrefix;

    RedisKeyPrefixEnum(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
}
