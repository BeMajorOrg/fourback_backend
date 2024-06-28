package com.fourback.bemajor.service;

import com.fourback.bemajor.domain.StudyGroup;
import com.fourback.bemajor.domain.StudyJoined;
import com.fourback.bemajor.domain.User;
import com.fourback.bemajor.dto.UserDto;
import com.fourback.bemajor.exception.NoSuchStudyGroupException;
import com.fourback.bemajor.repository.StudyGroupRepository;
import com.fourback.bemajor.repository.StudyJoinedRepository;
import com.fourback.bemajor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
            //예외처리
            //throw new NoSuchStudyGroupException()
        }
        if (userOptional.isEmpty()){
            //예외처리
        }
        studyJoinedRepository.save(new StudyJoined(studyGroupOptional.get(), userOptional.get()));
    }

    public void exitStudyGroup(Long studyJoinedId){
        studyJoinedRepository.deleteById(studyJoinedId);
    }

    public List<UserDto> getAllStudyUser(Long studyGroupId){
        Optional<StudyGroup> studyGroupOptional = studyGroupRepository.findById(studyGroupId);
        if (studyGroupOptional.isEmpty()){
            //예외처리
        }
        return studyGroupOptional.get().getStudyJoineds().stream().map(StudyJoined::getUser).map(User::toUserDto).collect(Collectors.toList());
    }
}
