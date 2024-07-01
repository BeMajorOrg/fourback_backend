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

<<<<<<< HEAD
    @PostMapping("/login")
    public String login(@RequestBody UserDto userDto, HttpServletResponse response) {
        String registrationId = userDto.getRegistrationId();
        String oauth2Id = registrationId + userDto.getUserId();
        boolean isMale = false;
        if (userDto.getGender().equals("male")) {
            isMale = true;
        }
        Optional<User> byOauth2Id = seniorUserService.findByOauth2Id(oauth2Id);
        User seniorUser;
        if (byOauth2Id.isEmpty()) {
            seniorUser = User.builder()
                    .userAge(LocalDateTime.now().getYear() - Integer.parseInt(userDto.getBirthYear()) + 1)
                    .userName(userDto.getName())
                    .email(userDto.getEmail()).role(Role.USER).oauth2Id(oauth2Id)
                    .isMale(isMale).build();
            seniorUserService.saveSeniorUser(seniorUser);
        } else {
            seniorUser = byOauth2Id.get();
        }
        response.addHeader("Authorization", "Bearer "+jwtUtil.createToken(oauth2Id, seniorUser.getRole(), 600*600*600L));
        return registrationId+"Login success";
=======
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
>>>>>>> 438cd582e1d50981cc362b97b98164e59f3de3b0
    }
}
