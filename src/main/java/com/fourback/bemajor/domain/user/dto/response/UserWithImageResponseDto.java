package com.fourback.bemajor.domain.user.dto.response;

import com.fourback.bemajor.domain.user.dto.request.UserRequestDto;
import lombok.*;

@Getter
@Setter
public class UserWithImageResponseDto extends UserRequestDto {
    private String imageName;
    private boolean isDeleted;

    public UserWithImageResponseDto(String userName, String email, String birth, String belong,
                                    String department, String hobby, String objective,
                                    String address, String techStack, boolean isDeleted) {
        super(userName, email, birth, belong, department, hobby, objective, address, techStack);
        this.isDeleted=isDeleted;
    }
}
