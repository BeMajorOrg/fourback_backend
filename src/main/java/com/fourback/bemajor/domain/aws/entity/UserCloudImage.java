package com.fourback.bemajor.domain.aws.entity;

import com.fourback.bemajor.domain.user.entity.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 유저 클라우드 이미지 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
public class UserCloudImage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String imagePath;
  @OneToOne(fetch = FetchType.LAZY)
  @Setter
  private UserEntity user;

  public UserCloudImage(Long imageId, String imagePath) {
    this.id = imageId;
    this.imagePath = imagePath;
  }
}
