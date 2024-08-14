package com.fourback.bemajor.domain.user.dto.response;

import com.fourback.bemajor.domain.user.dto.request.UserRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto extends UserRequestDto {
    private boolean isDeleted;

    public UserResponseDto(String userName, String email, String birth, String belong,
                                    String department, String hobby, String objective,
                                    String address, String techStack, boolean isDeleted) {
        super(userName, email, birth, belong, department, hobby, objective, address, techStack);
        this.isDeleted=isDeleted;
    }
}
