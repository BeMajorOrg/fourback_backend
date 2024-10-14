package com.fourback.bemajor.domain.studyGroupNotification.repository;

import com.fourback.bemajor.domain.studyGroupNotification.entitiy.StudyGroupNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudyGroupNotificationRepository
        extends JpaRepository<StudyGroupNotificationEntity, Long> {
    @Query("SELECT a.user.userId FROM StudyJoined a WHERE a.studyGroup.id = ?1 and a.user.userId <> ?2")
    Long[] findByStudyGroupIdNotUserId(Long studyGroupId, Long userId);

    @Query("select n from StudyGroupNotificationEntity n " +
            "join fetch n.user join fetch n.studyGroup where n.id=?1")
    Optional<StudyGroupNotificationEntity> findByIdWithUserAndStudyGroup(Long id);


    @Modifying
    @Query("delete from StudyGroupNotificationEntity n where n.studyGroup.id=?1")
    void deleteAllByStudyGroupId(Long studyGroupId);

    boolean existsByStudyGroupIdAndUserUserId(Long studyGroupId, Long userId);
}
