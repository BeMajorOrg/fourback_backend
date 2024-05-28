package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.Board;
import com.fourback.bemajor.domain.FavoriteBoard;

import com.fourback.bemajor.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface FavoriteBoardRepository extends JpaRepository<FavoriteBoard, Long> {

    List<FavoriteBoard> findByUserUserId(Long id);
    Optional<FavoriteBoard> findByUserAndBoard(User user, Board board);
}
