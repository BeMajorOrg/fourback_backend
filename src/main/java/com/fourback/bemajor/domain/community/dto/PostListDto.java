package com.fourback.bemajor.domain.community.dto;

import com.fourback.bemajor.domain.community.entity.Post;
import com.fourback.bemajor.domain.community.entity.ImageEntity;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


@Data
public class PostListDto {
    Long id;
    String title;
    String content;
    String memberName;
    String belong;
    String department;
    String profileImage;
    String postDate;
    int goodCount;
    int commentCount;
    int viewCount;
    String boardName;
    List<String> imageName;
    boolean postGood;
    boolean userCheck;


    public PostListDto(Post post, String postDate, List<ImageEntity> imageList, boolean postGood, boolean userCheck) {
        id = post.getId();
        title = post.getTitle();
        content = post.getContent();
        memberName = post.getUser().getUserName();
        department = post.getUser().getDepartment();
        belong = post.getUser().getBelong();
        profileImage = post.getUser().getImageUrl();
        goodCount = post.getGoodCount();
        commentCount = post.getCommentCount();
        viewCount = post.getViewCount();
        this.postDate = postDate;
        boardName = post.getBoard().getBoardName();
        imageName = imageList.stream()
                .map(ImageEntity::getImageUrl)
                .collect(Collectors.toList());
        this.postGood = postGood;
        this.userCheck = userCheck;
    }

}
