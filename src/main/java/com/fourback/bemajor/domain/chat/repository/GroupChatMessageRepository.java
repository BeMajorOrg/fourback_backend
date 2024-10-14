package com.fourback.bemajor.domain.chat.repository;

import com.fourback.bemajor.domain.chat.entitiy.GroupChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupChatMessageRepository extends JpaRepository<GroupChatMessageEntity, Long> {
    List<GroupChatMessageEntity> findByReceiverIdAndStudyGroupId(Long receiverId, Long studyGroupId);

    void deleteByReceiverIdAndStudyGroupId(Long receiverId, Long studyGroupId);

    void deleteMessagesByReceiverId(Long userId);
}
