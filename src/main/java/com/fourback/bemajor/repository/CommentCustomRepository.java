package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.Comment;

import java.util.List;

public interface CommentCustomRepository {
    List<Comment> findCommentListOrderByIDAsc(Long postID);

    List<Comment> findCommentReplies(Long postId, Long CommentID);
}
