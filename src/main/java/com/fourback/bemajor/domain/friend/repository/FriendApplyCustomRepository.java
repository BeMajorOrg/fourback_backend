package com.fourback.bemajor.domain.friend.repository;

import com.fourback.bemajor.domain.friend.entity.Friend;
import com.fourback.bemajor.domain.friend.entity.FriendApply;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FriendApplyCustomRepository {
    FriendApply checkDuplicateFriendApply(Long userId, Long friendId);
}