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
public class UserForInvitationResponse {
    private Long userId;
    private String userName;

    public static UserForInvitationResponse fromUser(UserEntity user) {
        return UserForInvitationResponse.builder()
                .userId(user.getId())
                .userName(user.getUserName())
                .build();
    }
}