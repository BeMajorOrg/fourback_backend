package com.fourback.bemajor.service;

import com.fourback.bemajor.domain.*;
import com.fourback.bemajor.dto.*;

import com.fourback.bemajor.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteBoardRepository favoriteBoardRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final FavoriteCommentRepository favoriteCommentRepository;
    private final PostRepository postRepository;
    private final FavoritePostRepository favoritePostRepository;

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

    public AddFavoriteCommentResponse addFavoriteComment(long commentId, String oauth2Id) {
        Comment c = commentRepository.getById(commentId);
        User user = userRepository.findByOauth2Id(oauth2Id).orElse(null);
        FavoriteComment fc =  favoriteCommentRepository.findByCommentAndUser(c, user);

        if(fc == null) {
            fc = FavoriteComment.builder().
                    comment(c).
                    user(user).
                    isFavorite(true).
                    build();
        } else {
            fc.setFavorite(true);
        }
        favoriteCommentRepository.save(fc);

        c.setGoodCount(favoriteCommentRepository.findFavoriteCommentListByComment(c).size());
        commentRepository.save(c);


        AddFavoriteCommentResponse res = AddFavoriteCommentResponse.builder().
                id(fc.getId())
                .build();

        return res;
    }

    public DeleteFavoriteCommentResponse deleteFavoriteComment(long commentId, String oauth2Id) {
        Comment c = commentRepository.getById(commentId);
        User user = userRepository.findByOauth2Id(oauth2Id).orElse(null);
        FavoriteComment fc = favoriteCommentRepository.findByCommentAndUser(c, user);

        if(fc != null) {
            fc.setFavorite(false);
            favoriteCommentRepository.save(fc);
        }

        c.setGoodCount(favoriteCommentRepository.findFavoriteCommentListByComment(c).size());
        commentRepository.save(c);

        DeleteFavoriteCommentResponse res = DeleteFavoriteCommentResponse.builder().
                id(fc.getId())
                .build();
        return res;
    }

    public Boolean getFavoriteComment(long commentId, String oauth2Id) {
        Comment c = commentRepository.getById(commentId);
        User user = userRepository.findByOauth2Id(oauth2Id).orElse(null);
        FavoriteComment fc = favoriteCommentRepository.findByCommentAndUser(c, user);

        return fc.isFavorite();
    }

    @Transactional
    public void post(Long postId, String oauth2Id) {
        Post post = postRepository.findById(postId).orElse(null);
        User user = userRepository.findByOauth2Id(oauth2Id).orElse(null);
        Optional<FavoritePost> optionalFavoritePost = favoritePostRepository.findByUserAndPost(user, post);

        if(optionalFavoritePost.isPresent()){
            FavoritePost favoritePost = optionalFavoritePost.get();
            favoritePostRepository.delete(favoritePost);
            post.setGoodCount((post.getGoodCount() - 1));
        } else {
            FavoritePost favoritePost = new FavoritePost();
            favoritePost.setPost(post);
            favoritePost.setUser(user);
            favoritePostRepository.save(favoritePost);
            post.setGoodCount((post.getGoodCount() + 1));
        }

    }
}
