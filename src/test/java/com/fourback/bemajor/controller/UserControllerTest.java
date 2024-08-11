package com.fourback.bemajor.controller;

import com.fourback.bemajor.domain.user.controller.UserController;
import com.fourback.bemajor.domain.user.service.UserService;
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
public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @Mock
    private Principal principal;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void deleteUserTest() throws Exception {
        //given
        Mockito.when(principal.getName()).thenReturn("kakao123");
        Mockito.doNothing().when(userService).delete("kakao123");

        //when
        ResultActions resultActions = mockMvc.perform(delete("/user").principal(principal));

        //then
        resultActions.andExpect(status().isOk());
        Mockito.verify(userService).delete("kakao123");
    }
}
