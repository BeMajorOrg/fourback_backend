package com.fourback.bemajor.service;

import com.fourback.bemajor.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    public void deleteTest(){
        //given
        Mockito.doNothing().when(userRepository).deleteByOauth2Id("kakao123");

        //when
        userService.delete("kakao123");

        //then
        Mockito.verify(userRepository, Mockito.times(1)).deleteByOauth2Id("kakao123");
    }
}
