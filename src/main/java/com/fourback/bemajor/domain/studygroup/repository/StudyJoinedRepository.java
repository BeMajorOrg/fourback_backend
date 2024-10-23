package com.fourback.bemajor.domain.studygroup.repository;

import com.fourback.bemajor.domain.studygroup.entity.StudyJoined;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyJoinedRepository extends JpaRepository<StudyJoined, Long> {
    @Query("SELECT a.id FROM StudyJoined a JOIN a.studyGroup b JOIN a.user c WHERE b.id = :studyGroupID AND c.id = :userId")
    List<Long> findIdsByStudyGroupIdAndOauth2Id(@Param("studyGroupID") Long studyGroupID, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM StudyJoined a WHERE a.id IN :ids")
    void deleteByIds(@Param("ids") List<Long> ids);

    Optional<StudyJoined> findByUser_IdAndStudyGroup_Id(Long userId, Long studyGroupId);

    @Query("select j from StudyJoined j join fetch j.studyGroup where j.user.id=?1")
    List<StudyJoined> findByUserId(Long userId);

    @Query("select j from StudyJoined j join fetch j.user where j.studyGroup.id=?1")
    List<StudyJoined> findByStudyGroupId(Long studyGroupId);

    @Query("select j from StudyJoined  j join fetch j.user join fetch j.studyGroup " +
            "where j.user.id!=?1 and j.studyGroup.id=?2")
    List<StudyJoined> findAllByUserIdNotAndStudyGroupId(Long userId, Long studyGroupId);
}
