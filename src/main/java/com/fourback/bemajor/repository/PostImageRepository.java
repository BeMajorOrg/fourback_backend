package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.Image;
import com.fourback.bemajor.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findByPostId(Long id);
}
