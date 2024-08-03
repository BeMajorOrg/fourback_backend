package com.fourback.bemajor.domain.aws;

import com.fourback.bemajor.domain.Post;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 게시물 클라우드 이미지 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
public class PostCloudImage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String imagePath;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  @Setter
  private Post post;

  public PostCloudImage(Long imageId, String imagePath) {
    this.id = imageId;
    this.imagePath = imagePath;
  }
}
