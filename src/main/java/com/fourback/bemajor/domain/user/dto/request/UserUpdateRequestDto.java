package com.fourback.bemajor.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {
    private String userName;
    private String email;
    private String birth;
    private String belong;
    private String department;
    private String hobby;
    private String objective;
    private String address;
    private String techStack;
}
