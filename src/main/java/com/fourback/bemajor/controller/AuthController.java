package com.fourback.bemajor.controller;

import com.fourback.bemajor.dto.TokenDto;
import com.fourback.bemajor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> reissue(@RequestHeader("refresh") String refresh) {
        TokenDto tokenDto = authService.checkToken(refresh);
        return ResponseEntity.ok()
                .header(tokenDto.getAccessToken(), tokenDto.getRefreshToken()).build();
    }
}
