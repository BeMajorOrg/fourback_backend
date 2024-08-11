package com.fourback.bemajor.domain.studygroup.repository;

import com.fourback.bemajor.domain.studygroup.entity.StudyJoined;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyJoinedRepository extends JpaRepository<StudyJoined, Long> {
    @Query("SELECT a.id FROM StudyJoined a JOIN a.studyGroup b JOIN a.user c WHERE b.id = :studyGroupID AND c.oauth2Id = :oauth2Id")
    List<Long> findIdsByStudyGroupIdAndOauth2Id(@Param("studyGroupID") Long studyGroupID, @Param("oauth2Id") String oauth2Id);

    @Modifying
    @Query("DELETE FROM StudyJoined a WHERE a.id IN :ids")
    void deleteByIds(@Param("ids") List<Long> ids);
}
