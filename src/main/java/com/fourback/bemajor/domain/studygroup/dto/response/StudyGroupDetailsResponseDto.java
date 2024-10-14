package com.fourback.bemajor.domain.studygroup.dto.response;

import com.fourback.bemajor.domain.user.dto.response.UserResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudyGroupDetailsResponseDto {
    private List<UserResponseDto> users;
    private boolean isEnableNotification;

    public static StudyGroupDetailsResponseDto of(
            List<UserResponseDto> users, boolean isEnableNotification ) {
        return StudyGroupDetailsResponseDto.builder()
                .users(users)
                .isEnableNotification(isEnableNotification)
                .build();
    }
}
