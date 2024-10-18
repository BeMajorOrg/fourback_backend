package com.fourback.bemajor.domain.studygroup.dto.response;

import com.fourback.bemajor.domain.studygroup.entity.StudyDetailGoal;
import com.fourback.bemajor.domain.studygroup.entity.StudyGoal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class StudyGroupGoalDetailResponse {
    private Long id;
    private String name;
    private Boolean checked;

    public static StudyGroupGoalDetailResponse fromEntity(StudyDetailGoal studyDetailGoal){
        return new StudyGroupGoalDetailResponse(studyDetailGoal.getId(),studyDetailGoal.getDetailGoalName(), studyDetailGoal.getValid());
    }
}
