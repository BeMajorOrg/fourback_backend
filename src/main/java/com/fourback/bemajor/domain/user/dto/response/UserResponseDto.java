package com.fourback.bemajor.domain.user.dto.response;

import com.fourback.bemajor.domain.user.dto.request.UserRequestDto;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    String userName;
    String email;
    String birth;
    String belong;
    String department;
    String hobby;
    String objective;
    String address;
    String techStack;
    String fileName;
    private boolean isDeleted;
}
