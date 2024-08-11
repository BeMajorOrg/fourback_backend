package com.fourback.bemajor.domain.image.repository;

import com.fourback.bemajor.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,Long> {
    Optional<Image> findByFileName(String fileName);
}
