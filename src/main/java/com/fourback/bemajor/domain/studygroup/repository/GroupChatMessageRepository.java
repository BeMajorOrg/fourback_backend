package com.fourback.bemajor.domain.studygroup.repository;

import com.fourback.bemajor.domain.studygroup.entity.GroupChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupChatMessageRepository extends JpaRepository<GroupChatMessageEntity, Long> {
    List<GroupChatMessageEntity> findAllByReceiverIdAndStudyGroupId(Long receiverId, Long studyGroupId);

    @Modifying
    @Query("delete from GroupChatMessageEntity g where g.receiverId=?1 and g.studyGroupId=?2")
    void deleteAllByReceiverIdAndStudyGroupId(Long receiverId, Long studyGroupId);

    @Modifying
    @Query("delete from GroupChatMessageEntity g where g.receiverId=?1")
    void deleteAllByReceiverId(Long userId);

    @Modifying
    @Query("delete from GroupChatMessageEntity g where g.studyGroupId=?1")
    void deleteAllByStudyGroupId(Long studyGroupId);
}
