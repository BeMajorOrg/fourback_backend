package com.fourback.bemajor.controller;

import com.fourback.bemajor.dto.LoginUserDto;
import com.fourback.bemajor.dto.TokenDto;
import com.fourback.bemajor.dto.UserDto;
import com.fourback.bemajor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<?> updateUserInfo(
            @RequestParam String userName, @RequestParam String email, @RequestParam String birth,
            @RequestParam String belong, @RequestParam String department, @RequestParam String hobby,
            @RequestParam String objective, @RequestParam String address, @RequestParam String techStack,
            Principal principal, @RequestParam(required = false) MultipartFile file) throws IOException {
        userService.update(new UserDto(userName, email, birth, belong, department, hobby, objective, address, techStack,false),
                principal.getName(), file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(Principal principal) throws IOException {
        userService.delete(principal.getName());
        return ResponseEntity.ok().build();
    }
}

