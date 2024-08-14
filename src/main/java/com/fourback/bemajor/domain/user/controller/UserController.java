package com.fourback.bemajor.domain.user.controller;

import com.fourback.bemajor.domain.user.dto.request.UserLoginRequestDto;
import com.fourback.bemajor.domain.user.dto.request.UserRequestDto;
import com.fourback.bemajor.domain.user.dto.response.UserWithImageResponseDto;
import com.fourback.bemajor.domain.user.service.UserService;
import com.fourback.bemajor.global.common.response.Response;
import com.fourback.bemajor.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;


@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        HttpHeaders tokens = userService.save(userLoginRequestDto);
        return Response.onSuccess(tokens);

    }

    @GetMapping
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        UserWithImageResponseDto userWithImageResponseDto = userService.get(customUserDetails.getUserId());
        return Response.onSuccess(userWithImageResponseDto);
    }

    @PatchMapping
    public ResponseEntity<?> updateUserInfo(@RequestBody UserRequestDto userRequestDto,
                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.update(userRequestDto, customUserDetails.getUserId());
        return Response.onSuccess();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        userService.delete(customUserDetails.getUserId());
        return Response.onSuccess();
    }
}

