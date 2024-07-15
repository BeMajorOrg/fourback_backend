package com.fourback.bemajor.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fourback.bemajor.domain.CommentType;
import com.fourback.bemajor.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.fourback.bemajor.domain.Comment;
import com.fourback.bemajor.domain.Post;

import java.time.Duration;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Data
public class CommentResult {
    private Long id;
    private User user;
    private String content;
    private int goodCount;
    private LocalDateTime commentDate;
    private long postId;
    private long parentId;
    private String dateDiff;
    private boolean isFavorite;
    private boolean userCheck;
    private int status;

    private GetCommentListResponse reply;

    public static CommentResult fromComment(Comment comment) {
        String commentContent = "";
        if(comment.getStatus() == CommentType.DEFAULT) {
            commentContent = comment.getContent();
        } else if(comment.getStatus() == CommentType.DELETED) {
            commentContent = "삭제된 댓글입니다.";
        }
        return CommentResult.builder()
                .id(comment.getId())
                .user(comment.getUser())
                .goodCount(comment.getGoodCount())
                .content(commentContent)
                .commentDate(comment.getCreatedDate())
                .build();
    }
}