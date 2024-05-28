package com.fourback.bemajor.service;

import com.fourback.bemajor.domain.User;
import com.fourback.bemajor.dto.FavoriteDto;
import com.fourback.bemajor.domain.Board;
import com.fourback.bemajor.domain.FavoriteBoard;

import com.fourback.bemajor.repository.BoardRepository;
import com.fourback.bemajor.repository.FavoriteBoardRepository;

import com.fourback.bemajor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteBoardRepository favoriteBoardRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void add(FavoriteDto favoriteDto, String oauth2Id) {
        Board board = boardRepository.findByBoardName(favoriteDto.getBoardName());
        User user = userRepository.findByOauth2Id(oauth2Id).orElse(null);
        Optional<FavoriteBoard> favoriteBoardOpt = favoriteBoardRepository.findByUserAndBoard(user, board);

        if(favoriteBoardOpt.isPresent()){
            FavoriteBoard favoriteBoard = favoriteBoardOpt.get();
            favoriteBoardRepository.delete(favoriteBoard);
        } else {
            FavoriteBoard favoriteBoard = new FavoriteBoard();
            favoriteBoard.setBoard(board);
            favoriteBoard.setUser(user);
            favoriteBoardRepository.save(favoriteBoard);
        }

    }

}
