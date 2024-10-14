package com.fourback.bemajor.domain.user.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String userName;
    private String email;
    private String birth;
    private String belong;
    private String department;
    private String hobby;
    private String objective;
    private String address;
    private String techStack;
    private String fileName;
    private boolean isDeleted;
}
