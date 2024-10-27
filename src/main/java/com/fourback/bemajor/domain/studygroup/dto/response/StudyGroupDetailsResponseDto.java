package com.fourback.bemajor.domain.studygroup.dto.response;

import com.fourback.bemajor.domain.user.dto.response.UserInquiryResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudyGroupDetailsResponseDto {
    private List<UserInquiryResponseDto> users;
    private Boolean isEnableNotification;

    public static StudyGroupDetailsResponseDto of(Boolean isEnableNotification,
                                                  List<UserInquiryResponseDto> users) {
        return StudyGroupDetailsResponseDto.builder()
                .users(users)
                .isEnableNotification(isEnableNotification)
                .build();
    }
}
