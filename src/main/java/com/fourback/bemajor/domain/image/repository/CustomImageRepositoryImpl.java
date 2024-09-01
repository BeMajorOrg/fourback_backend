package com.fourback.bemajor.domain.image.repository;

import com.fourback.bemajor.domain.image.entity.ImageEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.fourback.bemajor.domain.image.entity.QImageEntity.imageEntity;

@RequiredArgsConstructor
public class CustomImageRepositoryImpl implements CustomImageRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ImageEntity> findByFileNames(List<String> fileNames) {
        return jpaQueryFactory
                .selectFrom(imageEntity)
                .where(imageEntity.fileName.in(fileNames))
                .fetch();
    }
}
