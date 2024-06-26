package com.fourback.bemajor.controller;

import com.fourback.bemajor.dto.StudyGroupDto;
import com.fourback.bemajor.service.StudyGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
public class StudyGroupController {
    private final StudyGroupService studyGroupService;

    //추후 카테고리로 필터링하는 기능 추가할 예정
    @GetMapping("/studygroup")
    public List<StudyGroupDto> getStudyGroup(@RequestParam(defaultValue = "0") int page){
        return studyGroupService.getAllStudyGroup(page);
    }

    @PostMapping("/studygroup")
    public ResponseEntity<StudyGroupDto> createStudyGroup(StudyGroupDto studyGroupDto, Principal principal){
        studyGroupService.createStudyGroup(studyGroupDto, principal.getName());
        return ResponseEntity.ok(studyGroupDto);
    }

    @PutMapping("/studygroup")
    public ResponseEntity<StudyGroupDto> updateStudyGroup(Long studyGroupId, StudyGroupDto studyGroupDto, Principal principal){
        studyGroupService.updateStudyGroup(studyGroupId,studyGroupDto, principal.getName());
        return ResponseEntity.ok(studyGroupDto);
    }

    @DeleteMapping("/studygroup")
    public ResponseEntity<Void> deleteStudyGroup(Long studyGroupId, Principal principal){
        studyGroupService.deleteStudyGroup(studyGroupId,principal.getName());
        return ResponseEntity.ok().build();
    }
}
