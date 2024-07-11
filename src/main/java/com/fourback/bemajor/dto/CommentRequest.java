package com.fourback.bemajor.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fourback.bemajor.domain.Comment;
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