package com.fourback.bemajor.domain.user.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    Long userId;
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
