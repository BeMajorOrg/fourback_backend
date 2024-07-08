package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.FavoriteComment;
import com.fourback.bemajor.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FavoriteCommentRepository extends JpaRepository<FavoriteComment, Long>, FavoriteCommentCustomRepository{
    void deleteByCommentId(Long commentId);
    Optional<FavoriteComment> findByCommentId(Long commentId);
}
