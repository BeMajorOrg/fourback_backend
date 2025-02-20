package com.fourback.bemajor.domain.studygroup.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class StudyDetailGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String detailGoalName;
    private Boolean valid;
    @ManyToOne(fetch = FetchType.LAZY)
    private StudyGoal studyGoal;

    public StudyDetailGoal(String detailGoalName, StudyGoal studyGoal) {
        this.detailGoalName = detailGoalName;
        this.valid = false;
        this.studyGoal = studyGoal;
    }

    public void checkValid(Boolean check){
        this.valid = check;
    }


    public void registerStudyDetail(StudyGoal studyGoal){
        this.studyGoal = studyGoal;
    }
}
