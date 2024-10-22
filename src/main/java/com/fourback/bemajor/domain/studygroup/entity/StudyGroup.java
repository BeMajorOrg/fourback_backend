package com.fourback.bemajor.domain.studygroup.entity;

import com.fourback.bemajor.domain.studygroup.repository.StudyGoalRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
public class StudyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String studyName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer teamSize;
    private String studyLocation;
    private String category;
    private String studyCycle;
    private String studyRule;
    private Long ownerUserId;
    @OneToMany(mappedBy = "studyGroup", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<StudyJoined> studyJoineds = new ArrayList<>();

    @OneToMany(mappedBy = "studyGroup", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<StudyGoal> studyGoals;

    public void addStudyGoal(StudyGoal studyGoal){
        studyGoal.setStudyGroup(this);
        this.studyGoals.add(studyGoal);
    }

    public StudyGroup(String studyName,LocalDateTime startDate, LocalDateTime endDate, Integer teamSize, String studyLocation, String category, String studyCycle, String studyRule, Long ownerUserId) {
        this.studyName = studyName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teamSize = teamSize;
        this.studyLocation = studyLocation;
        this.category = category;
        this.studyCycle = studyCycle;
        this.studyRule = studyRule;
        this.ownerUserId = ownerUserId;
    }
    public void updateStudyGroup(String studyName,LocalDateTime startDate, LocalDateTime endDate, Integer teamSize, String studyLocation, String category, String studyCycle, String studyRule){
        this.studyName = studyName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teamSize = teamSize;
        this.studyLocation = studyLocation;
        this.category = category;
        this.studyCycle = studyCycle;
        this.studyRule = studyRule;
    }
}
