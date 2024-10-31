package com.fourback.bemajor.global.common.enums;

import lombok.Getter;

@Getter
public enum ExpiredTimeEnum {
    FCM(86460000L),
    ACCESS(600000L),
    REFRESH(86400000L);

    private final long expiredTime;

    ExpiredTimeEnum(long expiredTime) {
        this.expiredTime = expiredTime;
    }

}
