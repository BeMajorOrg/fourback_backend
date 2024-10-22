package com.fourback.bemajor.domain.comment.repository;

import com.fourback.bemajor.domain.comment.entity.FavoriteComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FavoriteCommentRepository extends JpaRepository<FavoriteComment, Long>, FavoriteCommentCustomRepository{
    void deleteByCommentId(Long commentId);
    List<FavoriteComment> findByCommentId(Long commentId);
}
