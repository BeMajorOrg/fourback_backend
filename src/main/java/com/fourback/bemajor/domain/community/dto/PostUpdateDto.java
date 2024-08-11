package com.fourback.bemajor.domain.community.dto;

import com.fourback.bemajor.domain.community.entity.Post;
import com.fourback.bemajor.domain.image.entity.PostImage;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostUpdateDto {
    String title;
    String content;
    List<String> imageName;
    String updateDate;

    public PostUpdateDto(Post post, String updateDate, List<PostImage> imageList) {
        title = post.getTitle();
        content = post.getContent();
        this.updateDate = updateDate;
        imageName = imageList.stream()
                .map(image -> image.getFileName())
                .collect(Collectors.toList());

    }
}
