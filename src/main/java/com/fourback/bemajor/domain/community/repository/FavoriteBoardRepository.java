package com.fourback.bemajor.domain.community.repository;

import com.fourback.bemajor.domain.community.entity.Board;
import com.fourback.bemajor.domain.community.entity.FavoriteBoard;

import com.fourback.bemajor.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface FavoriteBoardRepository extends JpaRepository<FavoriteBoard, Long> {

    List<FavoriteBoard> findByUserUserId(Long id);
    Optional<FavoriteBoard> findByUserAndBoard(UserEntity userEntity, Board board);
}
