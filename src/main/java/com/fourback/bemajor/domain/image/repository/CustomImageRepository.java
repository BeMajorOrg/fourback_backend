package com.fourback.bemajor.domain.image.repository;

import com.fourback.bemajor.domain.image.entity.ImageEntity;

import java.util.List;

public interface CustomImageRepository {
    List<ImageEntity> findByFileNames(List<String> fileNames);
}
