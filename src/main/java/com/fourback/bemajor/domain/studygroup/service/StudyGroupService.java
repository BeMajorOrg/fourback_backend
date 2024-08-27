package com.fourback.bemajor.domain.studygroup.service;

import com.fourback.bemajor.domain.studygroup.entity.StudyGroup;
import com.fourback.bemajor.domain.studygroup.entity.StudyJoined;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.studygroup.dto.StudyGroupDto;
import com.fourback.bemajor.global.exception.kind.NoSuchStudyGroupException;
import com.fourback.bemajor.global.exception.kind.NoSuchUserException;
import com.fourback.bemajor.global.exception.kind.NotAuthorizedException;
import com.fourback.bemajor.domain.studygroup.repository.StudyGroupRepository;
import com.fourback.bemajor.domain.studygroup.repository.StudyJoinedRepository;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;
    private final UserRepository userRepository;
    private final StudyJoinedRepository studyJoinedRepository;
    private final Map<Long, Set<WebSocketSession>> websocketSessionsMap;

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
        if (byOauth2Id.isEmpty()) throw new NoSuchUserException(6,"no such user",HttpStatus.BAD_REQUEST);
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
            throw new NoSuchStudyGroupException(5,"no such study group. can't delete", HttpStatus.BAD_REQUEST);
        }
        StudyGroup studyGroup = studyGroupOp.get();
        studyGroup.updateStudyGroup(studyGroupDto.getStudyName(),studyGroupDto.getStartDate(),studyGroupDto.getEndDate(), studyGroupDto.getTeamSize(),studyGroupDto.getStudyLocation(), studyGroup.getCategory(), studyGroupDto.getStudyCycle(), studyGroupDto.getStudyRule(), studyGroupDto.getStudySchedule());
    }

    @Transactional
    public void deleteStudyGroup(Long studyGroupId, Long userId){
        Optional<StudyGroup> studyGroupOp = studyGroupRepository.findById(studyGroupId);
        if (studyGroupOp.isEmpty()){
            throw new NoSuchStudyGroupException(5,"no such study group. can't delete", HttpStatus.BAD_REQUEST);
        }
        if (!studyGroupOp.get().getOwnerUserId().equals(userId)){
            throw new NotAuthorizedException(3,"not authorized. can't delete",HttpStatus.FORBIDDEN);
        }
        studyGroupRepository.deleteById(studyGroupId);
        websocketSessionsMap.remove(studyGroupId);
    }
}
