package com.fourback.bemajor.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.fourback.bemajor.domain.Comment;
import com.fourback.bemajor.domain.Post;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CommentResult {
    private Long id;
    private String content;
    private int goodCount;
    private LocalDateTime commentDate;
    private Post post;
    private Comment parent;

    private GetCommentListResponse reply;

    public static CommentResult fromComment(Comment comment) {
        return CommentResult.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .goodCount(comment.getGoodCount())
                .commentDate(comment.getCreatedDate())
                .post(comment.getPost())
                .parent(comment.getParent())
                .build();
    }
}