package com.fourback.bemajor.domain.comment.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@Data
public class GetCommentListResponse {
    private List<CommentResult> result;
    private int size;
}