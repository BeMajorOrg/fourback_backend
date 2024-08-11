package com.fourback.bemajor.domain.community.repository;

import com.fourback.bemajor.domain.community.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByBoardName(String name);
}
