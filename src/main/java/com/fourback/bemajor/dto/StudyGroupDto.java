package com.fourback.bemajor.dto;

import com.fourback.bemajor.domain.StudyGroup;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class StudyGroupDto {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer teamSize;
    private String studyLocation;
    private String category;
    private String studyCycle;
    private String studyRule;

    public static StudyGroupDto toDto(StudyGroup studyGroup){
        return new StudyGroupDto(
                studyGroup.getId(),
                studyGroup.getStartDate(),
                studyGroup.getEndDate(),
                studyGroup.getTeamSize(),
                studyGroup.getStudyLocation(),
                studyGroup.getCategory(),
                studyGroup.getStudyCycle(),
                studyGroup.getStudyRule());
    }

    public StudyGroup toEntity(String ownerOauth2Id){
        return new StudyGroup(
                startDate,
                endDate,
                teamSize,
                studyLocation,
                category,
                studyCycle,
                studyRule,
                ownerOauth2Id);
    }
}
