package com.fourback.bemajor.domain.community.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "image")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public static ImageEntity of(Post post, String imageUrl) {
        return ImageEntity.builder()
                .post(post)
                .imageUrl(imageUrl)
                .build();
    }
}
