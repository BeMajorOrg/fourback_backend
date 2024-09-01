package com.fourback.bemajor.domain.image.repository;

import com.fourback.bemajor.domain.image.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity, Long>, CustomImageRepository{
    List<ImageEntity> findByPostId(Long id);
}
