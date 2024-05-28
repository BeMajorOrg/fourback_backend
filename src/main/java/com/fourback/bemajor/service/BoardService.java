package com.fourback.bemajor.service;

import com.fourback.bemajor.domain.User;
import com.fourback.bemajor.dto.BoardDto;
import com.fourback.bemajor.domain.Board;
import com.fourback.bemajor.domain.FavoriteBoard;
import com.fourback.bemajor.repository.BoardRepository;
import com.fourback.bemajor.repository.FavoriteBoardRepository;
import com.fourback.bemajor.repository.UserRepository;
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
    public List<BoardDto> boards(String oauth2Id) {
        Optional<User> optionalUser = userRepository.findByOauth2Id(oauth2Id);
        User user = optionalUser.orElse(null);

        List<Board> boards = boardRepository.findAll();
        List<FavoriteBoard> favoriteBoards = favoriteBoardRepository.findByUserUserId(user.getUserId());
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
