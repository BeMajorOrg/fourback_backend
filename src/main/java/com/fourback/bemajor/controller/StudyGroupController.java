package com.fourback.bemajor.controller;

import com.fourback.bemajor.dto.StudyGroupDto;
import com.fourback.bemajor.dto.UserDto;
import com.fourback.bemajor.service.StudyGroupService;
import com.fourback.bemajor.service.StudyJoinedService;
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
    private final StudyJoinedService studyJoinedService;

    @GetMapping("/studygroup")
    public List<StudyGroupDto> getStudyGroup(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "category", defaultValue = "") String category){
        return studyGroupService.getAllStudyGroup(page,category);
    }

    @PostMapping("/studygroup")
    public ResponseEntity<StudyGroupDto> createStudyGroup(@RequestBody StudyGroupDto studyGroupDto, Principal principal){
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

    @PostMapping("/studygroup/joingroup/{studyGroupId}")
    public ResponseEntity<Void> joinGroup(@PathVariable("studyGroupId") Long groupId, Principal principal){
        studyJoinedService.joinStudyGroup(principal.getName(),groupId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/studygroup/exitgroup/{studyGroupId}")
    public ResponseEntity<Void> exitGroup(@PathVariable("studyGroupId") Long groupId,Principal principal){
        studyJoinedService.exitStudyGroup(groupId,principal.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/studygroup/members/{studyGroupId}")
    public ResponseEntity<List<UserDto>> getGroupMembers(@PathVariable("studyGroupId") Long studyGroupID,Principal principal){
        List<UserDto> allStudyUser = studyJoinedService.getAllStudyUser(studyGroupID);
        return ResponseEntity.ok(allStudyUser);
    }

    @GetMapping("/studygroup/mygroups")
    public ResponseEntity<List<StudyGroupDto>> myStudyGroupList(Principal principal){
        List<StudyGroupDto> allMyGroups = studyJoinedService.getAllMyGroups(principal.getName());
        return ResponseEntity.ok(allMyGroups);
    }
}
