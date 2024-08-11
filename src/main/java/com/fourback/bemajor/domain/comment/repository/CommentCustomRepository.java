package com.fourback.bemajor.domain.comment.repository;

import com.fourback.bemajor.domain.comment.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentCustomRepository {
    List<Comment> findCommentListOrderByIDAsc(Long postID);
    List<Comment> findCommentReplies(Long postID, Long CommentId);

}