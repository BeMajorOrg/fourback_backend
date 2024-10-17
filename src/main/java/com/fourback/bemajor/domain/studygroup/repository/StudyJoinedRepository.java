package com.fourback.bemajor.domain.studygroup.repository;

import com.fourback.bemajor.domain.studygroup.entity.StudyJoined;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyJoinedRepository extends JpaRepository<StudyJoined, Long> {
    @Query("SELECT a.id FROM StudyJoined a JOIN a.studyGroup b JOIN a.user c WHERE b.id = :studyGroupID AND c.userId = :userId")
    List<Long> findIdsByStudyGroupIdAndOauth2Id(@Param("studyGroupID") Long studyGroupID, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM StudyJoined a WHERE a.id IN :ids")
    void deleteByIds(@Param("ids") List<Long> ids);

    Optional<StudyJoined> findByUser_UserIdAndStudyGroup_Id(Long userId, Long studyGroupId);

    @Query("SELECT a.user.userId FROM StudyJoined a WHERE a.studyGroup.id = ?1 and a.user.userId <> ?2")
    Long[] findByStudyGroupIdNotUserId(Long studyGroupId, Long userId);

    @Query("select j from StudyJoined j join fetch j.studyGroup where j.user.userId=?1")
    List<StudyJoined> findByUserId(Long userId);
}
