package com.fourback.bemajor.domain.friend.repository;

import com.fourback.bemajor.domain.comment.entity.Comment;
import com.fourback.bemajor.domain.comment.repository.CommentCustomRepository;
import com.fourback.bemajor.domain.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FriendCustomRepository {
    Friend findFriendByUserIdAndFriendId(Long userId, Long friendId);
    Friend checkDuplicateFriend(Long userId, Long friendId);
    List<Friend> findFriendListOrderByUserIdDesc(Long userId);
}