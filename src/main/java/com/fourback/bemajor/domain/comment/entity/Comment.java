package com.fourback.bemajor.domain.comment.entity;

import com.fourback.bemajor.global.common.entity.BaseTimeEntity;
import com.fourback.bemajor.domain.community.entity.Post;
import com.fourback.bemajor.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @NotEmpty
    private String content;

    @ColumnDefault("0")
    private int goodCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Column(name = "status", nullable = false)
    @Builder.Default
    private CommentType status = CommentType.DEFAULT; // 댓글 상태 (0: 공개, 1: 삭제됨)

}
