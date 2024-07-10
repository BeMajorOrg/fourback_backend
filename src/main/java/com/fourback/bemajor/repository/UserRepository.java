package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByOauth2Id(String oauthId);
    void deleteByOauth2Id(String oauthId);
    @Query("select u from User u join fetch u.userImage where u.oauth2Id=?1")
    Optional<User> findByOauth2IdWithImage(String oauthId);
}
