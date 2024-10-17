package com.fourback.bemajor.domain.studygroup.repository;

import com.fourback.bemajor.domain.studygroup.entity.StudyGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyGoalRepository extends JpaRepository<StudyGoal, Long> {
    List<StudyGoal> findAllByStudyGroupId(Long studyGroupId);
}
