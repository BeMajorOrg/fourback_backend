package com.fourback.bemajor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    String userName;
    String email;
    String birth;
    String belong;
    String department;
    String hobby;
    String objective;
    String address;
    String techStack;
    boolean isDeleted;
}
