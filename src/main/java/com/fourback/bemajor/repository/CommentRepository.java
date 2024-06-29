package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.Comment;
import com.fourback.bemajor.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository{

    List<Comment> findByPostId(Long id);
    void deleteByPostId(Long postId);
    List<Comment> findByParentId(Long parentId);

}
