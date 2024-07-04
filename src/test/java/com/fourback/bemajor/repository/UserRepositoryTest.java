package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.User;
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
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void deleteByOauth2IdTest(){
        //given
        User user = User.builder()
                .oauth2Id("kakao123")
                .build();
        userRepository.save(user);

        //when
        userRepository.deleteByOauth2Id(user.getOauth2Id());

        //then
        Optional<User> ou = userRepository.findByOauth2Id(user.getOauth2Id());
        assertFalse(ou.isPresent());
    }
}
