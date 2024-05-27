package com.fourback.bemajor.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public class CommentRequest {
        @JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
        public record Add(
                Long postId,
                Long parentCommentId,
                String content) {
        }

        @JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
        public record Put(
                String content) {
        }
    }
