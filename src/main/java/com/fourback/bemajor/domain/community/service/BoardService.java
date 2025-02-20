package com.fourback.bemajor.domain.community.service;

import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.community.dto.BoardDto;
import com.fourback.bemajor.domain.community.entity.Board;
import com.fourback.bemajor.domain.community.entity.FavoriteBoard;
import com.fourback.bemajor.domain.community.repository.BoardRepository;
import com.fourback.bemajor.domain.community.repository.FavoriteBoardRepository;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final FavoriteBoardRepository favoriteBoardRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<BoardDto> boards(Long userId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        UserEntity userEntity = optionalUser.orElse(null);

        List<Board> boards = boardRepository.findAll();
        List<FavoriteBoard> favoriteBoards = favoriteBoardRepository.findByUserId(userEntity.getId());
        Set<Long> favoriteBoardIds = new HashSet<>();
        for (FavoriteBoard favoriteBoard : favoriteBoards) {
            Board board = favoriteBoard.getBoard();
            favoriteBoardIds.add(board.getId());
        }

        List<BoardDto> boardDtos = new ArrayList<>();

        for (FavoriteBoard favoriteBoard : favoriteBoards) {
            Board board = favoriteBoard.getBoard();
            boolean isFavorite = true; // 즐겨찾기 목록에 있는 경우는 항상 즐겨찾기 상태로 설정
            BoardDto boardDto = new BoardDto(board.getId(), board.getBoardName(), isFavorite);
            boardDtos.add(boardDto);
        }


        for (Board board : boards) {
            Long boardId = board.getId();
            if (!favoriteBoardIds.contains(boardId)) { // 즐겨찾기 목록에 없는 경우
                BoardDto boardDto = new BoardDto(boardId, board.getBoardName(), false); // 즐겨찾기 상태는 false로 설정
                boardDtos.add(boardDto);
            }
        }
        return boardDtos;

    }

}
