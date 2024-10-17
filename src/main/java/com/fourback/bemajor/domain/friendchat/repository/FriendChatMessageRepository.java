package com.fourback.bemajor.domain.friendchat.repository;

import com.fourback.bemajor.domain.friendchat.entity.FriendChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendChatMessageRepository extends JpaRepository<FriendChatMessageEntity, Long> {

    List<FriendChatMessageEntity> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    void deleteBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
