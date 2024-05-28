package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.Board;
import com.fourback.bemajor.domain.FavoriteBoard;
import com.fourback.bemajor.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface FavoriteBoardRepository extends JpaRepository<FavoriteBoard, Long> {

    List<FavoriteBoard> findByMemberId(Long id);
    Optional<FavoriteBoard> findByMemberAndBoard(Member member, Board board);
}
