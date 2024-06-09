package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.Board;
import lombok.Builder;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByBoardName(String name);
}
