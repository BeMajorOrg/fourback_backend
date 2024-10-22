package com.fourback.bemajor.domain.studygroup.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class StudyGroupAlarmDto {
    private String message;
    private String title;
    private String fcmToken;
}
