package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.ChatMessage;
import com.fourback.bemajor.domain.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("select c from ChatMessage c where c.StudyGroupId=?1 and c.oauth2Id=?2")
    List<ChatMessage> findByStudyGroupIdAndOauth2Id(Long studyGroupId, String oauth2Id);
    @Modifying
    @Query("delete from ChatMessage c where c.StudyGroupId=?1 and c.oauth2Id=?2")
    void deleteMessagesByStudyGroupIdAndOauth2Id(Long studyGroupId,String oauth2Id);
    void deleteMessagesByOauth2Id(String oauth2Id);
}
