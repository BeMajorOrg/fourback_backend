package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.Comment;
import com.fourback.bemajor.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository{
    int countByPost(Post post);
}
