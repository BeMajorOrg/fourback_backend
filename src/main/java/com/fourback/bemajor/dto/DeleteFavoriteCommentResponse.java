package com.fourback.bemajor.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@Data
public class DeleteFavoriteCommentResponse {
    private long id;
}
