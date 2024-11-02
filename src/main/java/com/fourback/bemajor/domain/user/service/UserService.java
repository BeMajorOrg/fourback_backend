package com.fourback.bemajor.domain.user.service;

import com.fourback.bemajor.domain.aws.service.S3UploadService;
import com.fourback.bemajor.domain.studygroup.service.GroupChatMessageService;
import com.fourback.bemajor.domain.studygroup.service.StudyJoinedService;
import com.fourback.bemajor.domain.user.dto.request.UserLoginRequestDto;
import com.fourback.bemajor.domain.user.dto.request.UserUpdateRequestDto;
import com.fourback.bemajor.domain.user.dto.response.UserInquiryResponseDto;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import com.fourback.bemajor.global.common.enums.FieldKeyEnum;
import com.fourback.bemajor.global.common.enums.KeyPrefixEnum;
import com.fourback.bemajor.global.common.service.RedisService;
import com.fourback.bemajor.global.exception.kind.NotFoundException;
import com.fourback.bemajor.global.security.jwt.JWTUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JWTUtil jwtUtil;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final S3UploadService s3UploadService;
    private final StudyJoinedService studyJoinedService;
    private final GroupChatMessageService groupChatMessageService;

    @Transactional
    public List<Pair<String, String>> save(UserLoginRequestDto requestDto) {
        String oauth2Id = requestDto.getRegistrationId() + requestDto.getUserId();
        UserEntity user = this.findOrCreateUser(oauth2Id);
        this.restoreUserIfDeleted(user);

        // refreshToken을 먼저 저장하고 fcm 토큰 저장해야 lru 조건 만족
        List<Pair<String, String>> tokens = jwtUtil.createTokens(user.getId(), user.getRole());

        redisService.putField(KeyPrefixEnum.TOKENS.getKeyPrefix() + user.getId(),
            FieldKeyEnum.FCM.getFieldKey(), requestDto.getFcmToken());

        return tokens;
    }

    public UserInquiryResponseDto get(Long userId) {
        UserEntity user = this.getUserById(userId);

        return user.toInquiryResponseDto();
    }

    @Transactional
    public void update(UserUpdateRequestDto requestDto, Long userId) {
        UserEntity user = this.getUserById(userId);

        user.modify(requestDto);
        userRepository.save(user);
    }

    @Transactional
    public void delete(Long userId) {
        UserEntity user = this.getUserById(userId);
        this.deleteImageFromS3(user);

        studyJoinedService.exitAll(userId);

        groupChatMessageService.deleteAllFromUser(userId);

        userRepository.delete(user);

        redisService.deleteKey(KeyPrefixEnum.TOKENS.getKeyPrefix() + userId);
    }

    @Transactional
    public String saveImage(MultipartFile file, Long userId) throws IOException {
        UserEntity user = this.getUserById(userId);

        String imageUrl = s3UploadService.saveFile(file);

        user.setImageUrl(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }

    @Transactional
    public void deleteImage(Long userId) {
        UserEntity user = this.getUserById(userId);

        s3UploadService.deleteFile(user.getImageUrl());

        user.setImageUrl(null);
        userRepository.save(user);
    }

    private UserEntity findOrCreateUser(String oauth2Id) {
        return userRepository.findByOauth2Id(oauth2Id)
                .orElseGet(() -> this.createUser(oauth2Id));
    }

    private UserEntity createUser(String oauth2Id) {
        UserEntity user = UserEntity.from(oauth2Id);
        return userRepository.save(user);
    }

    private void restoreUserIfDeleted(UserEntity user) {
        if (user.isDeleted()) {
            user.setDeleted(false);
            userRepository.save(user);
        }
    }

    private void deleteImageFromS3(UserEntity user) {
        String imageUrl = user.getImageUrl();
        s3UploadService.deleteFile(imageUrl);
    }

    private UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("This is not in user db"));
    }
}
