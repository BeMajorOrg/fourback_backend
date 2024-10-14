package com.fourback.bemajor.domain.studygroup.service;

import com.fourback.bemajor.domain.studyGroupNotification.repository.StudyGroupNotificationRepository;
import com.fourback.bemajor.domain.studygroup.dto.StudyGroupDto;
import com.fourback.bemajor.domain.studygroup.entity.StudyGroup;
import com.fourback.bemajor.domain.studygroup.entity.StudyJoined;
import com.fourback.bemajor.domain.studygroup.repository.StudyGroupRepository;
import com.fourback.bemajor.domain.studygroup.repository.StudyJoinedRepository;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import com.fourback.bemajor.global.exception.kind.NotAuthorizedException;
import com.fourback.bemajor.global.exception.kind.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;
    private final UserRepository userRepository;
    private final StudyJoinedRepository studyJoinedRepository;
    private final Map<Long, Set<WebSocketSession>> websocketSessionsMap;
    private final StudyGroupNotificationRepository studyGroupNotificationRepository;

    public List<StudyGroupDto> getAllStudyGroup(int page, String category){
        PageRequest pageable = PageRequest.of(page, 10, Sort.by("startDate").descending());
        Page<StudyGroup> studyGroups;
        if (category.isEmpty()) {
            studyGroups = studyGroupRepository.findAll(pageable);
        }
        else{
            studyGroups = studyGroupRepository.findAllByCategory(category,pageable);
        }
        List<StudyGroupDto> studyGroupDtoList = studyGroups.stream().map(StudyGroupDto::toDto).collect(Collectors.toList());
        return studyGroupDtoList;
    }

    @Transactional
    public void createStudyGroup(StudyGroupDto studyGroupDto, Long userId){
        StudyGroup studyGroup = studyGroupDto.toEntity(userId);
        StudyGroup savedGroup = studyGroupRepository.save(studyGroup);
        Optional<UserEntity> byOauth2Id = userRepository.findById(userId);
        if (byOauth2Id.isEmpty()) throw new NotFoundException("no such user");
        UserEntity userEntity = byOauth2Id.get();
        StudyJoined studyJoined = new StudyJoined(savedGroup, userEntity);
        studyJoinedRepository.save(studyJoined);
        studyGroupRepository.save(studyGroup);
        websocketSessionsMap.put(studyGroup.getId(), Collections.newSetFromMap(new ConcurrentHashMap<>()));
    }

    @Transactional
    public void updateStudyGroup(Long studyGroupId, StudyGroupDto studyGroupDto, Long userId){
        Optional<StudyGroup> studyGroupOp = studyGroupRepository.findById(studyGroupId);
        if (studyGroupOp.isEmpty()){
            throw new NotFoundException("no such study group. can't delete");
        }
        StudyGroup studyGroup = studyGroupOp.get();
        studyGroup.updateStudyGroup(studyGroupDto.getStudyName(),studyGroupDto.getStartDate(),studyGroupDto.getEndDate(), studyGroupDto.getTeamSize(),studyGroupDto.getStudyLocation(), studyGroup.getCategory(), studyGroupDto.getStudyCycle(), studyGroupDto.getStudyRule());
    }

    @Transactional
    public void deleteStudyGroup(Long studyGroupId, Long userId){
        Optional<StudyGroup> studyGroupOp = studyGroupRepository.findById(studyGroupId);
        if (studyGroupOp.isEmpty()){
            throw new NotFoundException("no such study group. can't delete");
        }
        if (!studyGroupOp.get().getOwnerUserId().equals(userId)){
            throw new NotAuthorizedException("not authorized. can't delete in studyGroup");
        }
        studyGroupNotificationRepository.deleteAllByStudyGroupId(studyGroupId);
        studyGroupRepository.deleteById(studyGroupId);
        websocketSessionsMap.remove(studyGroupId);
    }
}
