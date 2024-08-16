package com.fourback.bemajor.domain.user.service;

import com.fourback.bemajor.domain.studygroup.entity.StudyGroup;
import com.fourback.bemajor.domain.studygroup.repository.StudyGroupRepository;
import com.fourback.bemajor.global.common.service.ImageFileService;
import com.fourback.bemajor.domain.user.dto.request.UserLoginRequestDto;
import com.fourback.bemajor.domain.user.dto.request.UserUpdateRequestDto;
import com.fourback.bemajor.domain.user.dto.response.UserResponseDto;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import com.fourback.bemajor.global.common.service.RedisService;
import com.fourback.bemajor.global.exception.ExceptionEnum;
import com.fourback.bemajor.global.exception.kind.NotFoundElementException;
import com.fourback.bemajor.global.security.JWTUtil;
import com.fourback.bemajor.service.ChatMessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final ImageFileService imageFileService;
    private final RedisService redisService;
    private final StudyGroupRepository studyGroupRepository;
    private final ChatMessageService chatMessageService;

    @Transactional
    public List<Pair<String, String>> save(UserLoginRequestDto userLoginRequestDto) {
        String oauth2Id = userLoginRequestDto.getRegistrationId() + userLoginRequestDto.getUserId();
        Optional<UserEntity> ou = userRepository.findByOauth2Id(oauth2Id);
        UserEntity user;
        if (ou.isEmpty()) {
            user = UserEntity.builder()
                    .role("ROLE_USER").oauth2Id(oauth2Id)
                    .isDeleted(false).studyJoineds(new ArrayList<>())
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
        UserEntity user = this.findById(userId);
        return user.toUserResponseDto();
    }

    @Transactional
    public void update(UserUpdateRequestDto userUpdateRequestDto, Long userId) {
        UserEntity user = this.findById(userId);
        user.update(userUpdateRequestDto);
        userRepository.save(user);
    }

    @Transactional
    public void delete(Long userId) throws IOException {
        UserEntity user = this.findById(userId);
        String fileName = user.getFileName();
        if (fileName != null) {
            imageFileService.deleteImageFile(fileName);
        }
        List<StudyGroup> studyGroupList = studyGroupRepository.findAll();
        studyGroupList.forEach((studyGroup) -> {
            redisService.deleteDisConnectUser(Long.toString(studyGroup.getId()),userId);
        });
        chatMessageService.deleteMessages(oauth2Id);
        userRepository.delete(user);
        redisService.deleteRefreshToken(userId);
        redisService.deleteFcmToken(oauth2Id);
    }

    @Transactional
    public String saveImage(Long userId, MultipartFile file) throws IOException {
        UserEntity user = this.findById(userId);
        String uniqueFileName = imageFileService.saveImageFile(file);
        user.setFileName(uniqueFileName);
        userRepository.save(user);
        return uniqueFileName;
    }

    public void deleteImage(Long userId) throws IOException {
        UserEntity user = this.findById(userId);
        imageFileService.deleteImageFile(user.getFileName());
        user.setFileName(null);
        userRepository.save(user);
    }

    private UserEntity findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(),
                "This is not in DB", HttpStatus.LOCKED));
    }
}
