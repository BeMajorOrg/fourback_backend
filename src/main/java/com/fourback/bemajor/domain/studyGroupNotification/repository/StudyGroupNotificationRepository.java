package com.fourback.bemajor.domain.studyGroupNotification.repository;

import com.fourback.bemajor.domain.studyGroupNotification.entitiy.StudyGroupNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudyGroupNotificationRepository
        extends JpaRepository<StudyGroupNotificationEntity, Long> {
    @Query("SELECT a.user.userId FROM StudyJoined a WHERE a.studyGroup.id = ?1 and a.user.userId <> ?2")
    Long[] findByStudyGroupIdNotUserId(Long studyGroupId, Long userId);

    @Query("select n from StudyGroupNotificationEntity n join fetch n.user " +
            "join fetch n.studyGroup where n.studyGroup.id=?1 and n.user.userId=?2")
    Optional<StudyGroupNotificationEntity> findByStudyGroupIdAndUserIdWithUser(
            Long studyGroupId, Long userId);


    void deleteByStudyGroupId(Long studyGroupId);

    boolean existsByStudyGroupIdAndUserUserId(Long studyGroupId, Long userId);
}
