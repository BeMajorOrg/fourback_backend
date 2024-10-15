package com.fourback.bemajor.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
