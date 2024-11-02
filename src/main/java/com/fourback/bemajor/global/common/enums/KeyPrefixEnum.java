package com.fourback.bemajor.global.common.enums;

import lombok.Getter;

@Getter
public enum KeyPrefixEnum {
    TOKENS("tokens:"),
    DISCONNECTED("disConnected:"),
    LOGOUT_ACCESS("logoutAccess:");

    private final String keyPrefix;

    KeyPrefixEnum(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
}
