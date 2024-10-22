package com.fourback.bemajor.domain.friend.dto;

import com.fourback.bemajor.domain.user.entity.UserEntity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Data
public class FriendResponse {
    private Long userId;
    private String userName;
    private String email;
    private String birth;
    private String belong;
    private String department;
    private String hobby;
    private String objective;
    private String address;
    private String techstack;
    private String imageUrl;

    public static FriendResponse fromUser(UserEntity user) {
        return FriendResponse.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .birth(user.getBirth())
                .belong(user.getBelong())
                .department(user.getDepartment())
                .hobby(user.getHobby())
                .objective(user.getObjective())
                .address(user.getAddress())
                .techstack(user.getTechStack())
                .imageUrl(user.getImageUrl())
                .build();
    }
}