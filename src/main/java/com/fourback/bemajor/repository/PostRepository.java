package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p join p.user where p.board.id = :boardId")
    Page<Post> findAllWithPost(@Param("boardId") Long boardId, Pageable pageable);

    @Query("select p from Post p where p.content like %:keyword% or p.title like %:keyword%")
    Page<Post> findAllSearchPost(@Param("keyword") String keyword,Pageable pageable);

    @Query("select p from Post p join p.user where p.user.id = :userId")
    Page<Post> findAllMyPost(@Param("userId") Long userId,Pageable pageable);

}
