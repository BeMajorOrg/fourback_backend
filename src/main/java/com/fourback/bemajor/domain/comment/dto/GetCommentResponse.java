package com.fourback.bemajor.domain.comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
@Data
public class GetCommentResponse {
    CommentResult result;
}
