package com.fourback.bemajor.domain.user.repository;

import com.fourback.bemajor.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    //@SQLRestriction("is_deleted=false") 안 되게 하기
    @Query(value = "select * from user where oauth2_id=?1", nativeQuery = true)
    Optional<UserEntity> findByOauth2Id(String oauthId);
}
