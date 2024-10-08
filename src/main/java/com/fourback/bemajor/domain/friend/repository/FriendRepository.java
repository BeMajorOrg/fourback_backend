package com.fourback.bemajor.domain.friend.repository;

import com.fourback.bemajor.domain.comment.repository.CommentCustomRepository;
import com.fourback.bemajor.domain.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository  extends JpaRepository<Friend, Long>, FriendCustomRepository {

}