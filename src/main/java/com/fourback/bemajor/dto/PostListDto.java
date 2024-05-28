package com.fourback.bemajor.dto;

import com.fourback.bemajor.domain.Post;
import lombok.Data;


@Data
public class PostListDto {
    Long id;
    String title;
    String content;
    String memberName;
    String postDate;
    int goodCount;
    int commentCount;
    int viewCount;
    String boardName;

    public PostListDto(Post post, String postDate) {
        id = post.getId();
        title = post.getTitle();
        content = post.getContent();
        memberName = post.getUser().getUserName();
        goodCount = post.getGoodCount();
        viewCount = post.getViewCount();
        this.postDate = postDate;
        boardName = post.getBoard().getBoardName();


    }

}
