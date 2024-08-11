package com.fourback.bemajor.domain.user.service;

import com.fourback.bemajor.domain.user.entity.User;
import com.fourback.bemajor.domain.user.dto.LoginUserDto;
import com.fourback.bemajor.domain.user.dto.TokenDto;
import com.fourback.bemajor.domain.user.dto.UserDto;
import com.fourback.bemajor.global.exception.kind.NotFoundElementException;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import com.fourback.bemajor.domain.image.service.ImageFileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final ImageFileService imageFileService;

    @Transactional
    public User findByOauth2Id(String oauth2Id) {
        Optional<User> ou = userRepository.findByOauth2Id(oauth2Id);
        if (ou.isEmpty()) {
            throw new NotFoundElementException(1, "That is not in DB", HttpStatus.NOT_FOUND);
        }
        return ou.get();
    }

    @Transactional
    public User findByOauth2IdWithImage(String oauth2Id) {
        Optional<User> ou = userRepository.findByOauth2IdWithImage(oauth2Id);
        if (ou.isEmpty()) {
            throw new NotFoundElementException(1, "That is not in DB", HttpStatus.NOT_FOUND);
        }
        return ou.get();
    }

    @Transactional
    public TokenDto save(LoginUserDto loginUserDto) {
        String registrationId = loginUserDto.getRegistrationId();
        String oauth2Id = registrationId + loginUserDto.getUserId();
        Optional<User> ou = userRepository.findByOauth2Id(oauth2Id);
        User user;
        if (ou.isEmpty()) {
            user = User.builder()
                    .role("ROLE_USER")
                    .oauth2Id(oauth2Id)
                    .build();
            userRepository.save(user);
        } else {
            user = ou.get();
            if(user.isDeleted()){
                user.setDeleted(false);
                userRepository.save(user);
            }
        }
        return authService.newToken(user.getOauth2Id(), user.getRole());
    }

    @Transactional
    public UserDto get(String oauth2Id) {
        User user = findByOauth2IdWithImage(oauth2Id);
        return user.toUserWithImageDto();
    }

    @Transactional
    public void update(UserDto userDto, String oauth2Id) {
        User user = findByOauth2Id(oauth2Id);
        user.setUserDto(userDto);
        userRepository.save(user);
    }

    @Transactional
    public void delete(String oauth2Id) throws IOException {
        User user = findByOauth2IdWithImage(oauth2Id);
        if (user.getUserImage() != null) {
            imageFileService.deleteImageFile(user.getUserImage().getFilePath());
        }
        userRepository.delete(user);
    }
}
