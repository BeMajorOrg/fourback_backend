package com.fourback.bemajor.domain.user.dto.response;

import com.fourback.bemajor.domain.user.entity.UserEntity;
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

    public static UserInquiryResponseDto from(UserEntity user) {
        return UserInquiryResponseDto.builder()
                .userId(user.getId())
                .userName(user.getUserName())
                .address(user.getAddress())
                .department(user.getDepartment())
                .imageUrl(user.getImageUrl())
                .email(user.getEmail())
                .hobby(user.getHobby())
                .birth(user.getBirth())
                .belong(user.getBelong())
                .objective(user.getObjective())
                .techStack(user.getTechStack())
                .isDeleted(user.isDeleted())
                .build();
    }
}
