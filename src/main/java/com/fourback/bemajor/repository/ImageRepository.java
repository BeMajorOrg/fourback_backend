package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
