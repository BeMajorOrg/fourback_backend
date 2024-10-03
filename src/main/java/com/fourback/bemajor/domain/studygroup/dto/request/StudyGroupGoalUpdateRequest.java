package com.fourback.bemajor.domain.studygroup.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Getter
@NoArgsConstructor
public class StudyGroupGoalUpdateRequest {
    private Long goalId;
    private String goalName;
    private LocalDate endDate;
}
