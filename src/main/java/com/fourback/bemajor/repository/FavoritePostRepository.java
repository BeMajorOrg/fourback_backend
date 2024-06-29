package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoritePostRepository extends JpaRepository<FavoritePost, Long> {

    List<FavoritePost> findByPostId(Long id);

    Optional<FavoritePost> findByUserAndPost(User user, Post post);
    void deleteByPostId(Long postId);

    @Query("SELECT fp.post FROM FavoritePost fp JOIN fp.post p WHERE fp.user.Id = :userId")
    Page<Post> findFavoritePosts(@Param("userId") Long userId, PageRequest pageRequest);
}
