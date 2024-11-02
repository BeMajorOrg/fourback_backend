package com.fourback.bemajor.global.common.enums;

import lombok.Getter;

@Getter
public enum ExpiredTimeEnum {
    ACCESS(600000L),
    REFRESH(86400000L);

    private final long expiredTime;

    ExpiredTimeEnum(long expiredTime) {
        this.expiredTime = expiredTime;
    }

}
