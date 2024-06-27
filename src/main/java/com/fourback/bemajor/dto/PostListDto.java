package com.fourback.bemajor.dto;

import com.fourback.bemajor.domain.Image;
import com.fourback.bemajor.domain.Post;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


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
    List<String> imageName;

    public PostListDto(Post post, String postDate,List<Image> imageList) {
        id = post.getId();
        title = post.getTitle();
        content = post.getContent();
        memberName = post.getUser().getUserName();
        goodCount = post.getGoodCount();
        viewCount = post.getViewCount();
        this.postDate = postDate;
        boardName = post.getBoard().getBoardName();
        imageName = imageList.stream()
                .map(image -> image.getFileName())
                .collect(Collectors.toList());

    }

}
