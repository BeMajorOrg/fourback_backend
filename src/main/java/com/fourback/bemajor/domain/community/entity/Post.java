package com.fourback.bemajor.domain.community.entity;

import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;


    @ColumnDefault("0")
    private int viewCount;
    @ColumnDefault("0")
    private int goodCount;
    @ColumnDefault("0")
    private int commentCount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

}
