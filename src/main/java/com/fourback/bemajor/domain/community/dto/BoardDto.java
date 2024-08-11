package com.fourback.bemajor.domain.community.dto;

import lombok.Data;

@Data
public class BoardDto {

    private Long id;
    private String boardName;
    private boolean isfavorite;

    public BoardDto(Long id, String boardName, boolean isFavorite) {
        this.id = id;
        this.boardName = boardName;
        this.isfavorite = isFavorite;
    }
}
