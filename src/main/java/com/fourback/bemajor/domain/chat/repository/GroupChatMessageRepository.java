package com.fourback.bemajor.domain.chat.repository;

import com.fourback.bemajor.domain.chat.entitiy.GroupChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupChatMessageRepository extends JpaRepository<GroupChatMessageEntity, Long> {
    @Query("select c from GroupChatMessageEntity c where c.receiverId=?1 and c.studyGroupId=?2")
    List<GroupChatMessageEntity> findByStudyGroupIdAndReceiverId(Long receiverId, Long studyGroupId);

    @Modifying
    @Query("delete from GroupChatMessageEntity c where c.receiverId=?1 and c.studyGroupId=?2")
    void deleteMessagesByStudyGroupIdAndReceiverId(Long receiverId, Long studyGroupId);

    void deleteMessagesByUserId(Long userId);
}
