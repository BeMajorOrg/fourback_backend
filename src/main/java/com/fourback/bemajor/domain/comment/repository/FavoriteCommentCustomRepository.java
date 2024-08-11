package com.fourback.bemajor.domain.comment.repository;

import com.fourback.bemajor.domain.comment.entity.Comment;
import com.fourback.bemajor.domain.comment.entity.FavoriteComment;
import com.fourback.bemajor.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FavoriteCommentCustomRepository {
    FavoriteComment findByCommentAndUser(Comment comment, User user);
    List<FavoriteComment> findFavoriteCommentListByComment(Comment comment);
    List<FavoriteComment> findFavoriteCommentListByUser(User user);
}