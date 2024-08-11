package com.fourback.bemajor.domain.comment.dto;

import lombok.Data;

@Data
public class CommentRequest {
    public record Add(
            Long postId,
            Long parentCommentId,
            String content) {
        }

    public record Put(
            Long commentId,
            String content) {
    }
    }