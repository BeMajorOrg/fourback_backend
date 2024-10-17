package com.fourback.bemajor.domain.studygroup.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StudyGroupGoalDetailCreateRequest {
    private Long studyGroupGoalId;
    private String detailGoalName;
}
