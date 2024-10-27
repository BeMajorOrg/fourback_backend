package com.fourback.bemajor.domain.studygroup.repository;

import com.fourback.bemajor.domain.studygroup.entity.GroupChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupChatMessageRepository extends JpaRepository<GroupChatMessageEntity, Long> {
    List<GroupChatMessageEntity> findAllByReceiverIdAndStudyGroupId(Long receiverId, Long studyGroupId);

    void deleteByReceiverIdAndStudyGroupId(Long receiverId, Long studyGroupId);

    void deleteMessagesByReceiverId(Long userId);
}
