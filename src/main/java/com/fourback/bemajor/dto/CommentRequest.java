package com.fourback.bemajor.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fourback.bemajor.domain.Comment;

public class CommentRequest {
    @JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
    public record Add(
            Long postId,
            Long parentCommentId,
            String content) {
        }

    public record Put(
            String content) {
    }
    }