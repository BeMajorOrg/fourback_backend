package com.fourback.bemajor.domain.user.service;

import com.fourback.bemajor.global.common.service.ImageFileService;
import com.fourback.bemajor.domain.user.dto.request.UserLoginRequestDto;
import com.fourback.bemajor.domain.user.dto.request.UserRequestDto;
import com.fourback.bemajor.domain.user.dto.response.UserResponseDto;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import com.fourback.bemajor.global.exception.ExceptionEnum;
import com.fourback.bemajor.global.exception.kind.NotFoundElementException;
import com.fourback.bemajor.global.security.JWTUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final ImageFileService imageFileService;

    @Transactional
    public HttpHeaders save(UserLoginRequestDto userLoginRequestDto) {
        String registrationId = userLoginRequestDto.getRegistrationId();
        String oauth2Id = registrationId + userLoginRequestDto.getUserId();
        Optional<UserEntity> ou = userRepository.findByOauth2Id(oauth2Id);
        UserEntity user;
        if (ou.isEmpty()) {
            user = UserEntity.builder()
                    .role("ROLE_USER")
                    .oauth2Id(oauth2Id)
                    .isDeleted(false)
                    .build();
            userRepository.save(user);
        } else {
            user = ou.get();
            if (user.isDeleted()) {
                user.setDeleted(false);
                userRepository.save(user);
            }
        }
        return jwtUtil.createTokens(user.getUserId(), user.getRole());
    }

    public UserResponseDto get(Long userId) {
        UserEntity userEntity = findById(userId);
        return userEntity.toUserResponseDto();
    }

    @Transactional
    public void update(UserRequestDto userDto, Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(),
                "This is not in DB", HttpStatus.LOCKED));
        userEntity.setUserEntity(userDto);
        userRepository.save(userEntity);
    }

    @Transactional
    public void delete(Long userId) throws IOException {
        UserEntity userEntity = findById(userId);
        String fileName = userEntity.getFileName();
        if (fileName != null) {
            imageFileService.deleteImageFile(fileName);
        }
        userRepository.delete(userEntity);
    }

    public UserEntity findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(),
                "This is not in DB", HttpStatus.LOCKED));
    }

    @Transactional
    public String updateImage(Long userId, MultipartFile file) throws IOException {
        UserEntity user = this.findById(userId);
        String uniqueFileName = imageFileService.saveImageFile(file);
        user.setFileName(uniqueFileName);
        userRepository.save(user);
        return uniqueFileName;
    }
}
