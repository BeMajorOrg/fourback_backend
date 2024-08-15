package com.fourback.bemajor.domain.studygroup.controller;

import com.fourback.bemajor.domain.studygroup.dto.StudyGroupDto;
import com.fourback.bemajor.domain.studygroup.service.StudyGroupService;
import com.fourback.bemajor.domain.studygroup.service.StudyJoinedService;
import com.fourback.bemajor.domain.user.dto.response.UserResponseDto;
import com.fourback.bemajor.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<StudyGroupDto> createStudyGroup(@RequestBody StudyGroupDto studyGroupDto, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        studyGroupService.createStudyGroup(studyGroupDto, customUserDetails.getUserId());
        return ResponseEntity.ok(studyGroupDto);
    }

    @PutMapping("/studygroup/{studyGroupId}")
    public ResponseEntity<StudyGroupDto> updateStudyGroup(@PathVariable("studyGroupId") Long studyGroupId,@RequestBody StudyGroupDto studyGroupDto, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        studyGroupService.updateStudyGroup(studyGroupId,studyGroupDto, customUserDetails.getUserId());
        return ResponseEntity.ok(studyGroupDto);
    }

    @DeleteMapping("/studygroup/{studyGroupId}")
    public ResponseEntity<Void> deleteStudyGroup(@PathVariable("studyGroupId") Long studyGroupId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        studyGroupService.deleteStudyGroup(studyGroupId,customUserDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/studygroup/joingroup/{studyGroupId}")
    public ResponseEntity<Void> joinGroup(@PathVariable("studyGroupId") Long groupId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        studyJoinedService.joinStudyGroup(customUserDetails.getUserId(),groupId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/studygroup/exitgroup/{studyGroupId}")
    public ResponseEntity<Void> exitGroup(@PathVariable("studyGroupId") Long groupId,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        studyJoinedService.exitStudyGroup(groupId,customUserDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/studygroup/members/{studyGroupId}")
    public ResponseEntity<List<UserResponseDto>> getGroupMembers(@PathVariable("studyGroupId") Long studyGroupID, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<UserResponseDto> allStudyUser = studyJoinedService.getAllStudyUser(studyGroupID);
        return ResponseEntity.ok(allStudyUser);
    }

    @GetMapping("/studygroup/mygroups")
    public ResponseEntity<List<StudyGroupDto>> myStudyGroupList(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<StudyGroupDto> allMyGroups = studyJoinedService.getAllMyGroups(customUserDetails.getUserId());
        return ResponseEntity.ok(allMyGroups);
    }
}
