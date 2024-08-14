package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserEntityRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void deleteByOauth2IdTest(){
        //given
        UserEntity userEntity = UserEntity.builder()
                .oauth2Id("kakao123")
                .build();
        userRepository.save(userEntity);

        //when
        userRepository.deleteByOauth2Id(userEntity.getOauth2Id());

        //then
        Optional<UserEntity> ou = userRepository.findByOauth2Id(userEntity.getOauth2Id());
        assertFalse(ou.isPresent());
    }
}
