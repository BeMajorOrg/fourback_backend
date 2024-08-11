package com.fourback.bemajor.domain.image.dto;

import com.fourback.bemajor.domain.user.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWithImageDto extends UserDto {
    private String imageName;
    private boolean isDeleted;
}
