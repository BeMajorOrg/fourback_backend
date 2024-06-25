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
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;

    public List<StudyGroupDto> getAllStudyGroup(int page){
        PageRequest pageable = PageRequest.of(page, 10, Sort.by("startDate").descending());
        Page<StudyGroup> studyGroups = studyGroupRepository.findAll(pageable);
        List<StudyGroupDto> studyGroupDtoList = studyGroups.stream().map(StudyGroupDto::toDto).collect(Collectors.toList());
        return studyGroupDtoList;
    }

    @Transactional
    public void createStudyGroup(StudyGroupDto studyGroupDto, String oauth2Id){
        StudyGroup studyGroup = studyGroupDto.toEntity(oauth2Id);
        studyGroupRepository.save(studyGroup);
    }

    @Transactional
    public void updateStudyGroup(Long studyGroupId, StudyGroupDto studyGroupDto, String oauth2Id){
        Optional<StudyGroup> studyGroupOp = studyGroupRepository.findById(studyGroupId);
        if (studyGroupOp.isEmpty()){
            throw new NoSuchStudyGroupException(5,"no such study group. can't delete", HttpStatus.BAD_REQUEST);
        }
        StudyGroup studyGroup = studyGroupOp.get();
        studyGroup.updateStudyGroup(studyGroupDto.getStartDate(),studyGroupDto.getEndDate(), studyGroupDto.getTeamSize(),studyGroupDto.getStudyLocation(), studyGroup.getCategory(), studyGroupDto.getStudyCycle(), studyGroup.getStudyRule());
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
    }
}
