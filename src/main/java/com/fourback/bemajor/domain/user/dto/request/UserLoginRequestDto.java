package com.fourback.bemajor.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDto {
    private String userId;
    private String registrationId;
}
