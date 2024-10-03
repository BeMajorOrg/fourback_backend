package com.fourback.bemajor.domain.studygroup.dto.response;

import com.fourback.bemajor.domain.studygroup.entity.StudyDetailGoal;
import com.fourback.bemajor.domain.studygroup.entity.StudyGoal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class StudyGroupGoalResponse {
    private String name;
    private LocalDate endDate;
    private Integer percentage;

    public static StudyGroupGoalResponse fromEntity(StudyGoal studyGoal){
        List<StudyDetailGoal> detailGoals = studyGoal.getDetailGoals();
        long finished = detailGoals.stream().filter(dgs -> dgs.getValid()).count();
        Integer percentage = (int) finished * 100 / detailGoals.size();
        return new StudyGroupGoalResponse(studyGoal.getGoalName(), studyGoal.getEndDate(), Integer.valueOf(percentage));
    }
}
