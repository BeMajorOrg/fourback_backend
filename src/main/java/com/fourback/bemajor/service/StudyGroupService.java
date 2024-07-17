package com.fourback.bemajor.service;

import com.fourback.bemajor.domain.StudyGroup;
import com.fourback.bemajor.dto.StudyGroupDto;
import com.fourback.bemajor.exception.NoSuchStudyGroupException;
import com.fourback.bemajor.exception.NotAuthorizedException;
import com.fourback.bemajor.repository.StudyGroupRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;
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
    public void createStudyGroup(StudyGroupDto studyGroupDto, String oauth2Id){
        StudyGroup studyGroup = studyGroupDto.toEntity(oauth2Id);
        studyGroupRepository.save(studyGroup);
        websocketSessionsMap.put(studyGroup.getId(), Collections.newSetFromMap(new ConcurrentHashMap<>()));
    }

    @Transactional
    public void updateStudyGroup(Long studyGroupId, StudyGroupDto studyGroupDto, String oauth2Id){
        Optional<StudyGroup> studyGroupOp = studyGroupRepository.findById(studyGroupId);
        if (studyGroupOp.isEmpty()){
            throw new NoSuchStudyGroupException(5,"no such study group. can't delete", HttpStatus.BAD_REQUEST);
        }
        StudyGroup studyGroup = studyGroupOp.get();
        studyGroup.updateStudyGroup(studyGroupDto.getStudyName(),studyGroupDto.getStartDate(),studyGroupDto.getEndDate(), studyGroupDto.getTeamSize(),studyGroupDto.getStudyLocation(), studyGroup.getCategory(), studyGroupDto.getStudyCycle(), studyGroup.getStudyRule());
    }

    @Transactional
    public void deleteStudyGroup(Long studyGroupId, String oauth2Id){
        Optional<StudyGroup> studyGroupOp = studyGroupRepository.findById(studyGroupId);
        if (studyGroupOp.isEmpty()){
            throw new NoSuchStudyGroupException(5,"no such study group. can't delete", HttpStatus.BAD_REQUEST);
        }
        if (!studyGroupOp.get().getOwnerOauth2Id().equals(oauth2Id)){
            throw new NotAuthorizedException(3,"not authorized. can't delete",HttpStatus.FORBIDDEN);
        }
        studyGroupRepository.deleteById(studyGroupId);
        websocketSessionsMap.remove(studyGroupId);
    }
}
