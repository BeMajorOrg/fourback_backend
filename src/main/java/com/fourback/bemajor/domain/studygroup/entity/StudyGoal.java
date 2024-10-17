package com.fourback.bemajor.domain.studygroup.entity;

import com.fourback.bemajor.domain.studygroup.dto.request.StudyGroupGoalUpdateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class StudyGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String goalName;
    private LocalDate endDate;
    @OneToMany(mappedBy = "studyGoal", fetch = FetchType.LAZY)
    private List<StudyDetailGoal> detailGoals;

    @ManyToOne
    @Setter
    private StudyGroup studyGroup;

    /**
     * 스터디 목표 수정
     * @param studyGroupGoalUpdateRequest 목표 수정 요청
     */
    public void update(StudyGroupGoalUpdateRequest studyGroupGoalUpdateRequest){
        this.goalName = studyGroupGoalUpdateRequest.getGoalName();
        this.endDate = studyGroupGoalUpdateRequest.getEndDate();
    }

    /**
     * 스터디 세부 목표 추가
     * @param studyDetailGoal 세부목표
     */
    public void addStudyGoalDetail(StudyDetailGoal studyDetailGoal){
        studyDetailGoal.registerStudyDetail(this);
        detailGoals.add(studyDetailGoal);
    }

    public StudyGoal(String goalName, LocalDate endDate, StudyGroup studyGroup){
        this.goalName = goalName;
        this.endDate = endDate;
        this.studyGroup = studyGroup;
    }
}
