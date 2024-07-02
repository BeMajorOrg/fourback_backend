package com.fourback.bemajor.controller;

import com.fourback.bemajor.dto.TokenDto;
import com.fourback.bemajor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> reissue(@RequestHeader("refresh") String refresh) {
        TokenDto tokenDto = authService.checkToken(refresh);
        return ResponseEntity.ok()
                .header("access", tokenDto.getAccessToken())
                .header("refresh", tokenDto.getRefreshToken()).build();
    }
}
