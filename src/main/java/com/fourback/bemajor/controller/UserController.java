package com.fourback.bemajor.controller;

import com.fourback.bemajor.domain.UserDomain;
import com.fourback.bemajor.dto.UserDto;
import com.fourback.bemajor.enums.Role;
import com.fourback.bemajor.jwt.JWTUtil;
import com.fourback.bemajor.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService seniorUserService;
    private final JWTUtil jwtUtil;

    @GetMapping("/login")
    public String login(@RequestBody UserDto userDto, HttpServletResponse response) {
        String registrationId = userDto.getRegistrationId();
        String oauth2Id = registrationId + userDto.getUserId();
        boolean isMale = false;
        if (userDto.getGender().equals("male")) {
            isMale = true;
        }
        Optional<UserDomain> byOauth2Id = seniorUserService.findByOauth2Id(oauth2Id);
        UserDomain seniorUser;
        if (byOauth2Id.isEmpty()) {
            seniorUser = UserDomain.builder()
                    .userAge(LocalDateTime.now().getYear() - Integer.parseInt(userDto.getBirthYear()) + 1)
                    .userName(userDto.getName())
                    .email(userDto.getEmail()).role(Role.USER).oauth2Id(oauth2Id)
                    .isMale(isMale).build();
            seniorUserService.saveSeniorUser(seniorUser);
        } else {
            seniorUser = byOauth2Id.get();
        }
        response.addHeader("Authorization", "Bearer "+jwtUtil.createToken(oauth2Id, seniorUser.getRole(), 60*60*60L));
        return registrationId+"Login success";
    }
}
