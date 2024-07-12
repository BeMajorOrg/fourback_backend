package com.fourback.bemajor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWithImageDto extends UserDto{
    private String imageName;
    private boolean isDeleted;
}
