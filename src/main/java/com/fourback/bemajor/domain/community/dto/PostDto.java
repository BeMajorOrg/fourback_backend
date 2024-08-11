package com.fourback.bemajor.domain.community.dto;

import lombok.Data;

@Data
public class PostDto {
    private Long boardId;
    private String title;
    private String content;
    private String boardName;
}
