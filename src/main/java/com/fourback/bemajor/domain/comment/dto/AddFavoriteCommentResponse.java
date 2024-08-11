package com.fourback.bemajor.domain.comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@Data
public class AddFavoriteCommentResponse {
    private long id;
}
