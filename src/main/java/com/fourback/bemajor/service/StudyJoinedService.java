package com.fourback.bemajor.service;

import com.fourback.bemajor.domain.StudyGroup;
import com.fourback.bemajor.domain.StudyJoined;
import com.fourback.bemajor.domain.User;
import com.fourback.bemajor.dto.StudyGroupDto;
import com.fourback.bemajor.dto.UserDto;
import com.fourback.bemajor.exception.NoSuchStudyGroupException;
import com.fourback.bemajor.exception.NoSuchUserException;
import com.fourback.bemajor.repository.StudyGroupRepository;
import com.fourback.bemajor.repository.StudyJoinedRepository;
import com.fourback.bemajor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyJoinedService {
    private final StudyJoinedRepository studyJoinedRepository;
    private final UserRepository userRepository;
    private final StudyGroupRepository studyGroupRepository;

    public void joinStudyGroup(String userId, Long studyGroupId){
        Optional<StudyGroup> studyGroupOptional = studyGroupRepository.findById(studyGroupId);
        Optional<User> userOptional = userRepository.findByOauth2Id(userId);
        if (studyGroupOptional.isEmpty()){
            throw new NoSuchStudyGroupException(5,"no such study group.", HttpStatus.BAD_REQUEST);
        }
        if (userOptional.isEmpty()){
            throw new NoSuchUserException(6, "no such user.", HttpStatus.BAD_REQUEST);
        }
        studyJoinedRepository.save(new StudyJoined(studyGroupOptional.get(), userOptional.get()));
    }

    @Transactional
    public void exitStudyGroup(Long studyGroupId, String oauth2Id){
        List<Long> idsByStudyGroupIdAndOauth2Id = studyJoinedRepository.findIdsByStudyGroupIdAndOauth2Id(studyGroupId, oauth2Id);
        studyJoinedRepository.deleteByIds(idsByStudyGroupIdAndOauth2Id);
    }

    public List<UserDto> getAllStudyUser(Long studyGroupId){
        Optional<StudyGroup> studyGroupOptional = studyGroupRepository.findById(studyGroupId);
        if (studyGroupOptional.isEmpty()){
            throw new NoSuchStudyGroupException(5,"no such study group.", HttpStatus.BAD_REQUEST);
        }
        return studyGroupOptional.get().getStudyJoineds().stream().map(StudyJoined::getUser).map(User::toUserDto).collect(Collectors.toList());
    }

    public List<StudyGroupDto> getAllMyGroups(String oauth2Id){
        Optional<User> userOptional = userRepository.findByOauth2Id(oauth2Id);
        if (userOptional.isEmpty()){
            throw new NoSuchUserException(6, "no such user.", HttpStatus.BAD_REQUEST);
        }
        return userOptional.get().getStudyJoineds().stream().map(StudyJoined::getStudyGroup).map(StudyGroupDto::toDto).collect(Collectors.toList());
    }
}
