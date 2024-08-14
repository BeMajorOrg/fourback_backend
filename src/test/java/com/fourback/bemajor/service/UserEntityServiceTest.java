package com.fourback.bemajor.service;

import com.fourback.bemajor.domain.user.service.UserService;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class UserEntityServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    public void deleteTest() throws IOException {
        //given
        Mockito.doNothing().when(userRepository).deleteById(1L);

        //when
        userService.delete(1L);

        //then
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }
}
