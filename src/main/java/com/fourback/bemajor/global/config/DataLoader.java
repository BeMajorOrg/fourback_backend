package com.fourback.bemajor.global.config;

import com.fourback.bemajor.domain.community.entity.Board;
import com.fourback.bemajor.domain.community.repository.BoardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final BoardRepository boardRepository;

    public DataLoader(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (boardRepository.count() == 0) {  // 데이터가 없을 때만 삽입
            boardRepository.save(Board.builder().boardName("자유게시판").build());
            boardRepository.save(Board.builder().boardName("Q&A게시판").build());
            boardRepository.save(Board.builder().boardName("취업&진로게시판").build());
            boardRepository.save(Board.builder().boardName("모집게시판").build());
            boardRepository.save(Board.builder().boardName("공모전게시판").build());
            boardRepository.save(Board.builder().boardName("지식공유게시판").build());
        }
    }
}
