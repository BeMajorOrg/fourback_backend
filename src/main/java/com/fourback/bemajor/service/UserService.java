package com.fourback.bemajor.service;

import com.fourback.bemajor.domain.User;
import com.fourback.bemajor.dto.TokenDto;
import com.fourback.bemajor.dto.UserDto;
import com.fourback.bemajor.enums.Role;
import com.fourback.bemajor.exception.NotFoundElementException;
import com.fourback.bemajor.jwt.JWTUtil;
import com.fourback.bemajor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RedisService redisService;
    private final JWTUtil jwtUtil;
    private final AuthService authService;

    public User findByOauth2Id(String oauth2Id){
        Optional<User> ou = userRepository.findByOauth2Id(oauth2Id);
        if(ou.isEmpty()){
            throw new NotFoundElementException(1, "That is not in DB", HttpStatus.NOT_FOUND);
        }
        return ou.get();
    }

    public TokenDto save(UserDto userDto){
        String registrationId = userDto.getRegistrationId();
        String oauth2Id = registrationId + userDto.getUserId();
        Optional<User> ou = userRepository.findByOauth2Id(oauth2Id);
        User user;
        if (ou.isEmpty()) {
            user = User.builder()
                    .birth(userDto.getBirth())
                    .userName(userDto.getName())
                    .email(userDto.getEmail())
                    .role(Role.USER)
                    .oauth2Id(oauth2Id)
                    .build();
            userRepository.save(user);
        } else {
            user = ou.get();
        }
        return authService.newToken(oauth2Id, Role.USER);
    }
}
