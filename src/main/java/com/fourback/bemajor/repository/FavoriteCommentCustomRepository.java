package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.Comment;
import com.fourback.bemajor.domain.FavoriteComment;
import com.fourback.bemajor.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FavoriteCommentCustomRepository {
    FavoriteComment findByCommentAndUser(Comment comment, User user);
    List<FavoriteComment> findFavoriteCommentListByComment(Comment comment);
    List<FavoriteComment> findFavoriteCommentListByUser(User user);
}