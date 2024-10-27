package com.fourback.bemajor.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInquiryResponseDto {
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
    private String imageUrl;
    private boolean isDeleted;
}
