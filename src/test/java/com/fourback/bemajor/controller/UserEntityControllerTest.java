package com.fourback.bemajor.controller;

import com.fourback.bemajor.domain.user.controller.UserController;
import com.fourback.bemajor.domain.user.service.UserService;
import com.fourback.bemajor.global.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserEntityControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @Mock
    private CustomUserDetails customUserDetails;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void deleteUserTest() throws Exception {
        //given
        Mockito.when(customUserDetails.getUserId()).thenReturn(1L);
        Mockito.doNothing().when(userService).delete(1L);

        //when
        ResultActions resultActions = mockMvc.perform(delete("/user"));

        //then
        resultActions.andExpect(status().isOk());
        Mockito.verify(userService).delete(1L);
    }
}
