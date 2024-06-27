package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.FavoriteBoard;
import com.fourback.bemajor.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,Long> {

    List<Image> findByPostId(Long id);
    Optional<Image> findByFileName(String fileName);

}
