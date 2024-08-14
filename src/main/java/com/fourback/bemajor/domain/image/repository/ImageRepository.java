package com.fourback.bemajor.domain.image.repository;

import com.fourback.bemajor.domain.image.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity,Long> {
    Optional<ImageEntity> findByFileName(String fileName);
    List<ImageEntity> findByPostId(Long id);
}
