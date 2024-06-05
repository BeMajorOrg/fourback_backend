package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentCustomRepository {
    List<Comment> findCommentListOrderByIDAsc(Long postID);
    List<Comment> findCommentReplies(Long postID, Long CommentId);
}