package com.fourback.bemajor.service;

import com.fourback.bemajor.dto.FavoriteDto;
import com.fourback.bemajor.domain.Board;
import com.fourback.bemajor.domain.FavoriteBoard;
import com.fourback.bemajor.domain.Member;
import com.fourback.bemajor.repository.BoardRepository;
import com.fourback.bemajor.repository.FavoriteBoardRepository;
import com.fourback.bemajor.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteBoardRepository favoriteBoardRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void add(FavoriteDto favoriteDto) {
        Board board = boardRepository.findByBoardName(favoriteDto.getBoardName());
        Member member = memberRepository.findById(favoriteDto.getMemberId()).orElse(null);
        Optional<FavoriteBoard> favoriteBoardOpt = favoriteBoardRepository.findByMemberAndBoard(member, board);

        if(favoriteBoardOpt.isPresent()){
            FavoriteBoard favoriteBoard = favoriteBoardOpt.get();
            favoriteBoardRepository.delete(favoriteBoard);
        } else {
            FavoriteBoard favoriteBoard = new FavoriteBoard();
            favoriteBoard.setBoard(board);
            favoriteBoard.setMember(member);
            favoriteBoardRepository.save(favoriteBoard);
        }

    }

}
