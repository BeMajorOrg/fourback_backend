package com.fourback.bemajor.domain.friend.repository;

import com.fourback.bemajor.domain.friend.entity.FriendApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendApplyRepository extends JpaRepository<FriendApply, Long>, FriendApplyCustomRepository {
  Integer countAllByFriend_UserId(Long id);

  List<FriendApply> findAllByFriend_UserId(Long id);
}
