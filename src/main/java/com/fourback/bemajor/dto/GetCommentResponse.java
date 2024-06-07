package com.fourback.bemajor.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
@Data
public class GetCommentResponse {
    CommentResult result;
}
