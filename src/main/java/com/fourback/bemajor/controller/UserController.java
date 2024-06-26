package com.fourback.bemajor.controller;

import com.fourback.bemajor.dto.LoginUserDto;
import com.fourback.bemajor.dto.TokenDto;
import com.fourback.bemajor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody LoginUserDto loginUserDto) {
        TokenDto tokenDto = userService.save(loginUserDto);
        return ResponseEntity.ok()
                .header("access", tokenDto.getAccessToken())
                .header("refresh", tokenDto.getRefreshToken()).build();
    }

}
