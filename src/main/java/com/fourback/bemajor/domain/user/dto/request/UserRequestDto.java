package com.fourback.bemajor.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    String userName;
    String email;
    String birth;
    String belong;
    String department;
    String hobby;
    String objective;
    String address;
    String techStack;
}
