package com.fourback.bemajor.controller;

import com.fourback.bemajor.dto.LoginUserDto;
import com.fourback.bemajor.dto.TokenDto;
import com.fourback.bemajor.dto.UserDto;
import com.fourback.bemajor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


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

    @GetMapping
    public ResponseEntity<?> getUserInfo(Principal principal) {
        UserDto userDto = userService.get(principal.getName());
        return ResponseEntity.ok()
                .body(userDto);
    }

    @PutMapping
    public ResponseEntity<?> updateUserInfo(@RequestBody UserDto userDto, Principal principal) {
        userService.update(userDto, principal.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(Principal principal) {
        userService.delete(principal.getName());
        return ResponseEntity.ok().build();
    }
}
