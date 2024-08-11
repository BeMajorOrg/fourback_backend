package com.fourback.bemajor.domain.image.repository;

import com.fourback.bemajor.domain.image.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
}
