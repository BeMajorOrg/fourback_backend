package com.fourback.bemajor.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDto {
    @NotBlank
    private String userId;

    @NotBlank
    private String registrationId;

    @NotEmpty
    private String fcmToken;
}
