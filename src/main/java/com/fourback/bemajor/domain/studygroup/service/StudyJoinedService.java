package com.fourback.bemajor.domain.studygroup.service;

import com.fourback.bemajor.domain.chat.repository.GroupChatMessageRepository;
import com.fourback.bemajor.domain.studygroup.dto.StudyGroupDto;
import com.fourback.bemajor.domain.studygroup.entity.StudyGroup;
import com.fourback.bemajor.domain.studygroup.entity.StudyJoined;
import com.fourback.bemajor.domain.studygroup.repository.StudyGroupRepository;
import com.fourback.bemajor.domain.studygroup.repository.StudyJoinedRepository;
import com.fourback.bemajor.domain.user.dto.response.UserResponseDto;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import com.fourback.bemajor.global.common.enums.RedisKeyPrefixEnum;
import com.fourback.bemajor.global.common.service.RedisService;
import com.fourback.bemajor.global.exception.kind.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyJoinedService {
    private final StudyJoinedRepository studyJoinedRepository;
    private final UserRepository userRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final RedisService redisService;
    private final GroupChatMessageRepository groupChatMessageRepository;
    private final Map<Long, Set<WebSocketSession>> studyGrupIdSessionsMap;

    public void joinStudyGroup(Long userId, Long studyGroupId) {
        Optional<StudyGroup> studyGroupOptional = studyGroupRepository.findById(studyGroupId);
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (studyGroupOptional.isEmpty()) {
            throw new NotFoundException("no such study group.");
        }
        if (userOptional.isEmpty()) {
            throw new NotFoundException("no such user.");
        }
        studyJoinedRepository.save(new StudyJoined(studyGroupOptional.get(), userOptional.get()));
        if (!studyGrupIdSessionsMap.get(studyGroupId).isEmpty()) {
            redisService.addLongMember(RedisKeyPrefixEnum.DISCONNECTED, studyGroupId, userId);
        }
    }

    @Transactional
    public void exitStudyGroup(Long studyGroupId, Long userId) {
        List<Long> idsByStudyGroupIdAndOauth2Id = studyJoinedRepository.findIdsByStudyGroupIdAndOauth2Id(studyGroupId, userId);
        studyJoinedRepository.deleteByIds(idsByStudyGroupIdAndOauth2Id);
        if (!studyGrupIdSessionsMap.get(studyGroupId).isEmpty())
            redisService.removeLongMember(RedisKeyPrefixEnum.DISCONNECTED, studyGroupId, userId);
        groupChatMessageRepository.deleteMessagesByStudyGroupIdAndReceiverId(userId, studyGroupId);
    }

    public List<UserResponseDto> getAllStudyUser(Long studyGroupId) {
        Optional<StudyGroup> studyGroupOptional = studyGroupRepository.findById(studyGroupId);
        if (studyGroupOptional.isEmpty()) {
            throw new NotFoundException("no such study group.");
        }
        return studyGroupOptional.get().getStudyJoineds().stream().map(StudyJoined::getUser).map(UserEntity::toUserResponseDto).collect(Collectors.toList());
    }

    public List<StudyGroupDto> getAllMyGroups(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("no such user.");
        }
        return userOptional.get().getStudyJoineds().stream().map(StudyJoined::getStudyGroup).map(StudyGroupDto::toDto).collect(Collectors.toList());
    }
}
