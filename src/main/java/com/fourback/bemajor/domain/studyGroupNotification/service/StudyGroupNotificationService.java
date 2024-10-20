package com.fourback.bemajor.domain.studyGroupNotification.service;

import com.fourback.bemajor.domain.studyGroupNotification.entitiy.StudyGroupNotificationEntity;
import com.fourback.bemajor.domain.studyGroupNotification.repository.StudyGroupNotificationRepository;
import com.fourback.bemajor.domain.studygroup.entity.StudyGroup;
import com.fourback.bemajor.domain.studygroup.repository.StudyGroupRepository;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import com.fourback.bemajor.global.common.enums.RedisKeyPrefixEnum;
import com.fourback.bemajor.global.common.service.RedisService;
import com.fourback.bemajor.global.exception.kind.NotAuthorizedException;
import com.fourback.bemajor.global.exception.kind.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudyGroupNotificationService {
    private final StudyGroupNotificationRepository studyGroupNotificationRepository;
    private final UserRepository userServiceRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final Map<Long, Set<WebSocketSession>> studyGrupIdSessionsMap;
    private final RedisService redisService;

    @Transactional
    public void enableNotification(Long studyGroupId, Long userId) {
        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new NotFoundException("no such study group notification"));
        UserEntity user = userServiceRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("no such study group notification"));
        StudyGroupNotificationEntity notificationEntity = StudyGroupNotificationEntity.builder()
                .studyGroup(studyGroup).user(user)
                .build();
        studyGroupNotificationRepository.save(notificationEntity);
    }

    @Transactional
    public void enableRealTimeNotification(Long studyGroupId, Long userId) {
        this.enableNotification(studyGroupId, userId);
        if (!studyGrupIdSessionsMap.get(studyGroupId).isEmpty()) {
            redisService.addLongMember(RedisKeyPrefixEnum.DISCONNECTED, studyGroupId, userId);
        }
    }

    @Transactional
    public void disableNotification(Long studyGroupId, Long userId) {
        StudyGroupNotificationEntity notificationEntity = studyGroupNotificationRepository
                .findByStudyGroupIdAndUserIdWithUser(studyGroupId, userId).orElseThrow(()
                        -> new NotFoundException("no such study group notification"));
        if(!notificationEntity.getUser().getUserId().equals(userId)){
            throw new NotAuthorizedException("not authorized. can't delete in studyGroupNotification");
        }
        studyGroupNotificationRepository.delete(notificationEntity);
        if (!studyGrupIdSessionsMap.get(studyGroupId).isEmpty())
            redisService.removeLongMember(RedisKeyPrefixEnum.DISCONNECTED, studyGroupId, userId);
    }
}
