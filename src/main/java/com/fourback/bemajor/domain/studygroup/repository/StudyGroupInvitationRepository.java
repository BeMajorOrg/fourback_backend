package com.fourback.bemajor.domain.studygroup.repository;

import com.fourback.bemajor.domain.studygroup.entity.StudyGroup;
import com.fourback.bemajor.domain.studygroup.entity.StudyGroupInvitation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 스터디 그룹 초대 리포지토리
 */
public interface StudyGroupInvitationRepository extends JpaRepository<StudyGroupInvitation, Long> {
  Integer countAllByUser_Id(Long id);

  List<StudyGroupInvitation> findAllByUser_Id(Long id);

  void deleteByStudyGroup(StudyGroup studyGroup);
}
