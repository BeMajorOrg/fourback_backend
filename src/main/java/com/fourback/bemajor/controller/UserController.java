package com.fourback.bemajor.controller;

import com.fourback.bemajor.domain.User;
import com.fourback.bemajor.dto.UserDto;
import com.fourback.bemajor.enums.Role;
import com.fourback.bemajor.jwt.JWTUtil;
import com.fourback.bemajor.service.RedisService;
import com.fourback.bemajor.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService seniorUserService;
    private final JWTUtil jwtUtil;
    private final RedisService redisService;

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
        String newRefresh = jwtUtil.createToken("refresh",oauth2Id,seniorUser.getRole(),86400000L);
        response.addHeader("access", jwtUtil.createToken("access",oauth2Id, seniorUser.getRole(), 600000L));
        response.addHeader("refresh", newRefresh);

        redisService.setRefreshToken(oauth2Id,newRefresh,86400000L);

        return registrationId+"Login success";
    }
}
