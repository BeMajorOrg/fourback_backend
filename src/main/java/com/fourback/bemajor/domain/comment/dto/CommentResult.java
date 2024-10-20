package com.fourback.bemajor.domain.comment.dto;

import com.fourback.bemajor.domain.comment.dto.GetCommentListResponse;
import com.fourback.bemajor.domain.comment.entity.CommentType;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.fourback.bemajor.domain.comment.entity.Comment;

@Builder
@Getter
@Setter
@Data
public class CommentResult {
    private Long id;
    private String content;
    private int goodCount;
    private String commentDate;
    private long postId;
    private long parentId;
    private String dateDiff;
    private boolean isFavorite;
    private boolean userCheck;
    private int status;

    private String profileImage;
    private String userName;
    private String email;
    private String birth;
    private String oauth2Id;
    private String role;
    private String belong;
    private String department;

    private GetCommentListResponse reply;

    public static CommentResult fromComment(Comment comment) {
        String commentContent = "";
        if(comment.getStatus() == CommentType.DEFAULT) {
            commentContent = comment.getContent();
        } else if(comment.getStatus() == CommentType.DELETED) {
            commentContent = "삭제된 댓글입니다.";
        }
        return CommentResult.builder()
                .id(comment.getId())
                .goodCount(comment.getGoodCount())
                .profileImage(comment.getUser().getImageUrl())
                .content(commentContent)
                .commentDate(comment.getCreatedDate().toString())
                .userName(comment.getUser().getUserName())
                .email(comment.getUser().getEmail())
                .birth(comment.getUser().getBirth())
                .oauth2Id(comment.getUser().getOauth2Id())
                .role(comment.getUser().getRole())
                .belong(comment.getUser().getBelong())
                .department(comment.getUser().getDepartment())
                .build();
    }
}