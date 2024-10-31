package com.fourback.bemajor.domain.user.repository;

import com.fourback.bemajor.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByOauth2Id(String oauthId);
}
