package com.fourback.bemajor.controller;

import com.fourback.bemajor.dto.StudyGroupDto;
import com.fourback.bemajor.service.StudyGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@RestController
public class StudyGroupController {
    private final StudyGroupService studyGroupService;

    //추후 카테고리로 필터링하는 기능 추가할 예정
    @GetMapping("/studygroup")
    public List<StudyGroupDto> getStudyGroup(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "category", defaultValue = "") String category){
        return studyGroupService.getAllStudyGroup(page,category);
    }

    @PostMapping("/studygroup")
    public ResponseEntity<StudyGroupDto> createStudyGroup(@RequestBody StudyGroupDto studyGroupDto, Principal principal){
        log.info("studygroup-post");
        studyGroupService.createStudyGroup(studyGroupDto, principal.getName());
        return ResponseEntity.ok(studyGroupDto);
    }

    @PutMapping("/studygroup/{studyGroupId}")
    public ResponseEntity<StudyGroupDto> updateStudyGroup(@PathVariable("studyGroupId") Long studyGroupId,@RequestBody StudyGroupDto studyGroupDto, Principal principal){
        studyGroupService.updateStudyGroup(studyGroupId,studyGroupDto, principal.getName());
        return ResponseEntity.ok(studyGroupDto);
    }

    @DeleteMapping("/studygroup/{studyGroupId}")
    public ResponseEntity<Void> deleteStudyGroup(@PathVariable("studyGroupId") Long studyGroupId, Principal principal){
        studyGroupService.deleteStudyGroup(studyGroupId,principal.getName());
        return ResponseEntity.ok().build();
    }
}
