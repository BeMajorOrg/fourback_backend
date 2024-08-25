package com.fourback.bemajor.domain.studygroup.dto;

import com.fourback.bemajor.domain.studygroup.entity.StudyGroup;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StudyGroupDto {
    private Long id;
    private String studyName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer teamSize;
    private String studyLocation;
    private String category;
    private String studyCycle;
    private String studyRule;
    private List<String> studySchedule;

    public static StudyGroupDto toDto(StudyGroup studyGroup){
        return new StudyGroupDto(
                studyGroup.getId(),
                studyGroup.getStudyName(),
                studyGroup.getStartDate(),
                studyGroup.getEndDate(),
                studyGroup.getTeamSize(),
                studyGroup.getStudyLocation(),
                studyGroup.getCategory(),
                studyGroup.getStudyCycle(),
                studyGroup.getStudyRule(),
                studyGroup.getStudySchedule());
    }

    public StudyGroup toEntity(String ownerOauth2Id){
        return new StudyGroup(
                studyName,
                startDate,
                endDate,
                teamSize,
                studyLocation,
                category,
                studyCycle,
                studyRule,
                studySchedule,
                ownerOauth2Id);
    }
}
