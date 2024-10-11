package com.fourback.bemajor.domain.studygroup.repository;

import com.fourback.bemajor.domain.studygroup.entity.StudyDetailGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyDetailGoalRepository extends JpaRepository<StudyDetailGoal, Long> {
    List<StudyDetailGoal> findAllByStudyGoal_Id(Long id);
}
