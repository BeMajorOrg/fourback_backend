package com.fourback.bemajor.domain.studygroup.repository;

import com.fourback.bemajor.domain.studygroup.entity.StudyJoinApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyJoinApplicationRepository extends JpaRepository<StudyJoinApplication, Long> {
    List<StudyJoinApplication> findAllByStudyGroup_Id(Long studyGroupId);
    Long countByStudyGroup_Id(Long studyGroupId);
}
