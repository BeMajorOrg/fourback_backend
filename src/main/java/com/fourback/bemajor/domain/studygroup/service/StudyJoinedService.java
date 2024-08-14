package com.fourback.bemajor.domain.studygroup.service;

import com.fourback.bemajor.domain.studygroup.entity.StudyGroup;
import com.fourback.bemajor.domain.studygroup.entity.StudyJoined;
import com.fourback.bemajor.domain.user.dto.response.UserResponseDto;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.studygroup.dto.StudyGroupDto;
import com.fourback.bemajor.domain.user.dto.request.UserRequestDto;
import com.fourback.bemajor.global.exception.kind.NoSuchStudyGroupException;
import com.fourback.bemajor.global.exception.kind.NoSuchUserException;
import com.fourback.bemajor.domain.studygroup.repository.StudyGroupRepository;
import com.fourback.bemajor.domain.studygroup.repository.StudyJoinedRepository;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyJoinedService {
    private final StudyJoinedRepository studyJoinedRepository;
    private final UserRepository userRepository;
    private final StudyGroupRepository studyGroupRepository;

    public void joinStudyGroup(Long userId, Long studyGroupId){
        Optional<StudyGroup> studyGroupOptional = studyGroupRepository.findById(studyGroupId);
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (studyGroupOptional.isEmpty()){
            throw new NoSuchStudyGroupException(5,"no such study group.", HttpStatus.BAD_REQUEST);
        }
        if (userOptional.isEmpty()){
            throw new NoSuchUserException(6, "no such user.", HttpStatus.BAD_REQUEST);
        }
        studyJoinedRepository.save(new StudyJoined(studyGroupOptional.get(), userOptional.get()));
    }

    @Transactional
    public void exitStudyGroup(Long studyGroupId, Long userId){
        List<Long> idsByStudyGroupIdAndOauth2Id = studyJoinedRepository.findIdsByStudyGroupIdAndOauth2Id(studyGroupId, userId);
        studyJoinedRepository.deleteByIds(idsByStudyGroupIdAndOauth2Id);
    }

    public List<UserResponseDto> getAllStudyUser(Long studyGroupId){
        Optional<StudyGroup> studyGroupOptional = studyGroupRepository.findById(studyGroupId);
        if (studyGroupOptional.isEmpty()){
            throw new NoSuchStudyGroupException(5,"no such study group.", HttpStatus.BAD_REQUEST);
        }
        return studyGroupOptional.get().getStudyJoineds().stream().map(StudyJoined::getUser).map(UserEntity::toUserResponseDto).collect(Collectors.toList());
    }

    public List<StudyGroupDto> getAllMyGroups(Long userId){
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()){
            throw new NoSuchUserException(6, "no such user.", HttpStatus.BAD_REQUEST);
        }
        return userOptional.get().getStudyJoineds().stream().map(StudyJoined::getStudyGroup).map(StudyGroupDto::toDto).collect(Collectors.toList());
    }
}
