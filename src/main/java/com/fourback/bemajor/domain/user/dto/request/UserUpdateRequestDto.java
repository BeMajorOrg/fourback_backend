package com.fourback.bemajor.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {
    @NotBlank
    private String userName;

    @NotBlank
    private String email;

    @NotBlank
    private String birth;

    @NotBlank
    private String belong;

    @NotBlank
    private String department;

    @NotBlank
    private String hobby;

    @NotBlank
    private String objective;

    @NotBlank
    private String address;

    @NotBlank
    private String techStack;
}
