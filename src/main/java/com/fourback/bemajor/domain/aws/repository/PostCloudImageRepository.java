package com.fourback.bemajor.domain.aws.repository;


import com.fourback.bemajor.domain.aws.entity.PostCloudImage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 클라우드 이미지 리포지토리
 */
public interface PostCloudImageRepository extends JpaRepository<PostCloudImage, Long> {
}
