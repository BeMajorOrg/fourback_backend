package com.fourback.bemajor.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Getter
public class StudyGroup {
    @Id
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer teamSize;
    private String studyLocation;
    private String category;
    private String studyCycle;
    private String studyRule;
    private String ownerOauth2Id;

    public StudyGroup(LocalDateTime startDate, LocalDateTime endDate, Integer teamSize, String studyLocation, String category, String studyCycle, String studyRule, String ownerOauth2Id) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.teamSize = teamSize;
        this.studyLocation = studyLocation;
        this.category = category;
        this.studyCycle = studyCycle;
        this.studyRule = studyRule;
        this.ownerOauth2Id = ownerOauth2Id;
    }
    public void updateStudyGroup(LocalDateTime startDate, LocalDateTime endDate, Integer teamSize, String studyLocation, String category, String studyCycle, String studyRule){
        this.startDate = startDate;
        this.endDate = endDate;
        this.teamSize = teamSize;
        this.studyLocation = studyLocation;
        this.category = category;
        this.studyCycle = studyCycle;
        this.studyRule = studyRule;
    }
}
