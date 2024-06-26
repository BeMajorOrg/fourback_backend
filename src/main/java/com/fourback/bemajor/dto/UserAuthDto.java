package com.fourback.bemajor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserAuthDto {
    private String oauth2Id;
    private String role;
}
