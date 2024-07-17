package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.ChatMessage;
import com.fourback.bemajor.domain.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByStudyGroupIdAndOauth2Id(Long studyGroupId, String oauth2Id);
    void deleteMessagesByStudyGroupIdAndOauth2Id(Long studyGroupId,String oauth2Id);
}
