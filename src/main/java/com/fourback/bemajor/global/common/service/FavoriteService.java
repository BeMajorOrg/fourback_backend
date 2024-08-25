package com.fourback.bemajor.global.common.service;

import com.fourback.bemajor.domain.comment.dto.AddFavoriteCommentResponse;
import com.fourback.bemajor.domain.comment.dto.DeleteFavoriteCommentResponse;
import com.fourback.bemajor.domain.comment.entity.Comment;
import com.fourback.bemajor.domain.comment.entity.FavoriteComment;
import com.fourback.bemajor.domain.comment.repository.CommentRepository;
import com.fourback.bemajor.domain.comment.repository.FavoriteCommentRepository;
import com.fourback.bemajor.domain.community.dto.FavoriteDto;
import com.fourback.bemajor.domain.community.entity.Board;
import com.fourback.bemajor.domain.community.entity.FavoriteBoard;
import com.fourback.bemajor.domain.community.entity.FavoritePost;
import com.fourback.bemajor.domain.community.entity.Post;
import com.fourback.bemajor.domain.community.repository.BoardRepository;
import com.fourback.bemajor.domain.community.repository.FavoriteBoardRepository;
import com.fourback.bemajor.domain.community.repository.FavoritePostRepository;
import com.fourback.bemajor.domain.community.repository.PostRepository;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.user.repository.UserRepository;

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
    private final CommentRepository commentRepository;
    private final FavoriteCommentRepository favoriteCommentRepository;
    private final PostRepository postRepository;
    private final FavoritePostRepository favoritePostRepository;

    @Transactional
    public void add(FavoriteDto favoriteDto, Long userId) {
        Board board = boardRepository.findByBoardName(favoriteDto.getBoardName());
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        Optional<FavoriteBoard> favoriteBoardOpt = favoriteBoardRepository.findByUserAndBoard(userEntity, board);

        if(favoriteBoardOpt.isPresent()){
            FavoriteBoard favoriteBoard = favoriteBoardOpt.get();
            favoriteBoardRepository.delete(favoriteBoard);
        } else {
            FavoriteBoard favoriteBoard = new FavoriteBoard();
            favoriteBoard.setBoard(board);
            favoriteBoard.setUser(userEntity);
            favoriteBoardRepository.save(favoriteBoard);
        }

    }

    public AddFavoriteCommentResponse addFavoriteComment(long commentId, Long userId) {
        Comment c = commentRepository.getById(commentId);
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        FavoriteComment fc =  favoriteCommentRepository.findByCommentAndUser(c, userEntity);

        if(fc == null) {
            fc = FavoriteComment.builder().
                    comment(c).
                    user(userEntity).
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

    public DeleteFavoriteCommentResponse deleteFavoriteComment(long commentId, Long userId) {
        Comment c = commentRepository.getById(commentId);
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        FavoriteComment fc = favoriteCommentRepository.findByCommentAndUser(c, userEntity);

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

    public Boolean getFavoriteComment(long commentId, Long userId) {
        Comment c = commentRepository.getById(commentId);
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        FavoriteComment fc = favoriteCommentRepository.findByCommentAndUser(c, userEntity);

        return (fc != null) ? fc.isFavorite() : false;
    }

    @Transactional
    public void post(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElse(null);
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        Optional<FavoritePost> optionalFavoritePost = favoritePostRepository.findByUserAndPost(userEntity, post);

        if(optionalFavoritePost.isPresent()){
            FavoritePost favoritePost = optionalFavoritePost.get();
            favoritePostRepository.delete(favoritePost);
            post.setGoodCount((post.getGoodCount() - 1));
        } else {
            FavoritePost favoritePost = new FavoritePost();
            favoritePost.setPost(post);
            favoritePost.setUser(userEntity);
            favoritePostRepository.save(favoritePost);
            post.setGoodCount((post.getGoodCount() + 1));
        }

    }
}
