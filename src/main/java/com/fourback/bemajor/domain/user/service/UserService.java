package com.fourback.bemajor.domain.user.service;

import com.fourback.bemajor.domain.aws.service.S3UploadService;
import com.fourback.bemajor.domain.chat.repository.GroupChatMessageRepository;
import com.fourback.bemajor.domain.studygroup.entity.StudyJoined;
import com.fourback.bemajor.domain.studygroup.repository.StudyJoinedRepository;
import com.fourback.bemajor.domain.user.dto.request.FcmTokenUpdateDto;
import com.fourback.bemajor.global.common.enums.RedisKeyPrefixEnum;
import com.fourback.bemajor.domain.user.dto.request.UserLoginRequestDto;
import com.fourback.bemajor.domain.user.dto.request.UserUpdateRequestDto;
import com.fourback.bemajor.domain.user.dto.response.UserResponseDto;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import com.fourback.bemajor.global.common.service.RedisService;
import com.fourback.bemajor.global.exception.kind.NotFoundException;
import com.fourback.bemajor.global.security.jwt.JWTUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
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
    private final RedisService redisService;
    private final StudyJoinedRepository studyJoinedRepository;
    private final GroupChatMessageRepository groupChatMessageRepository;
    private final S3UploadService s3UploadService;

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
        redisService.setValue(RedisKeyPrefixEnum.FCM,
                user.getUserId(), userLoginRequestDto.getFcmToken());
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
        String imageUrl = user.getImageUrl();
        if (imageUrl != null) {
            s3UploadService.deleteFile(imageUrl);
        }
        //user가 join한 곳에 대해서만 삭제해주기 + redis에서도 지워줘야 함
        List<StudyJoined> userJoinedList = studyJoinedRepository.findByUserId(userId);
        List<Long> studyGroupIds = userJoinedList.stream().map(
                studyJoined -> studyJoined.getStudyGroup().getId()).toList();
//        redisService.removeUserInGroupSession(studyGroupIds, userId);
        studyJoinedRepository.deleteAllInBatch(userJoinedList);
        groupChatMessageRepository.deleteMessagesByReceiverId(userId);
        userRepository.delete(user);
        redisService.deleteKey(RedisKeyPrefixEnum.REFRESH, userId);
        redisService.deleteKey(RedisKeyPrefixEnum.FCM, userId);
    }

    @Transactional
    public String saveImage(Long userId, MultipartFile file) throws IOException {
        UserEntity user = this.findById(userId);
        String imageUrl = s3UploadService.saveFile(file);
        user.setImageUrl(imageUrl);
        userRepository.save(user);
        return imageUrl;
    }

    @Transactional
    public void deleteImage(Long userId) {
        UserEntity user = this.findById(userId);
        s3UploadService.deleteFile(user.getImageUrl());
        user.setImageUrl(null);
        userRepository.save(user);
    }

    public void updateFcmToken(Long userId, FcmTokenUpdateDto fcmTokenUpdateDto) {
        redisService.setValue(RedisKeyPrefixEnum.FCM,
                userId, fcmTokenUpdateDto.getFcmToken());
    }

    private UserEntity findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("This is not in user db"));
    }
}
