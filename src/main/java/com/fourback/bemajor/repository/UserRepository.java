package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDomain,Long> {
    Optional<UserDomain> findByEmail(String email);
    Optional<UserDomain> findByOauth2Id(String oauthId);
}
