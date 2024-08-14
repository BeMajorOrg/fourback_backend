package com.fourback.bemajor.domain.user.repository;

import com.fourback.bemajor.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByOauth2Id(String oauthId);
    void deleteByOauth2Id(String oauthId);
    @Query("select u from UserEntity u left join fetch u.userImage where u.userId=?1")
    Optional<UserEntity> findByIdWithImage(Long userId);
}
