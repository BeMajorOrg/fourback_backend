package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.FavoriteComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FavoriteCommentRepository extends JpaRepository<FavoriteComment, Long>, FavoriteCommentCustomRepository{

}
