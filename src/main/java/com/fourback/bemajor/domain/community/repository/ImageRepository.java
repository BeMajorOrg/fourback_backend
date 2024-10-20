package com.fourback.bemajor.domain.community.repository;

import com.fourback.bemajor.domain.community.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findByPostId(Long id);

    @Query("select i from ImageEntity i join fetch i.post where i.imageUrl in ?1")
    List<ImageEntity> findAllByImageUrlsWithPost(List<String> imageUrls);
}
