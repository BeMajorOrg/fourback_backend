package com.fourback.bemajor.domain.studygroup.controller;

import com.fourback.bemajor.domain.studygroup.dto.request.StudyGoalDetailCheckRequest;
import com.fourback.bemajor.domain.studygroup.dto.request.StudyGroupGoalCreateRequest;
import com.fourback.bemajor.domain.studygroup.dto.request.StudyGroupGoalDetailCreateRequest;
import com.fourback.bemajor.domain.studygroup.dto.request.StudyGroupGoalUpdateRequest;
import com.fourback.bemajor.domain.studygroup.dto.response.StudyGroupGoalDetailResponse;
import com.fourback.bemajor.domain.studygroup.dto.response.StudyGroupGoalResponse;
import com.fourback.bemajor.domain.studygroup.service.StudyGroupGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class StudyGroupGoalController {
    private final StudyGroupGoalService studyGroupGoalService;

    @PostMapping("/studygroup/goals")
    public ResponseEntity<Void> saveStudyGroupGoal(@RequestBody StudyGroupGoalCreateRequest request){
        studyGroupGoalService.saveGoal(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/studygroup/{studyGroupId}/goals")
    public ResponseEntity<List<StudyGroupGoalResponse>> getStudyGroupGoals(@PathVariable("studyGroupId") Long studyGroupId){
        List<StudyGroupGoalResponse> studyGroupGoals = studyGroupGoalService.getStudyGroupGoals(studyGroupId);
        return ResponseEntity.ok(studyGroupGoals);
    }

    @PutMapping("/studygroup/goals")
    public ResponseEntity<Void> updateStudyGroupGoal(@RequestBody StudyGroupGoalUpdateRequest studyGroupGoalUpdateRequest){
        studyGroupGoalService.updateGoal(studyGroupGoalUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/studygroup/goals/{goalId}")
    public ResponseEntity<Void> deleteStudyGroupGoal(@PathVariable("goalId") Long goalId){
        studyGroupGoalService.deleteGoal(goalId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/studygroup/goals/details")
    public ResponseEntity<Void> saveStudyGroupGoalDetail(@RequestBody StudyGroupGoalDetailCreateRequest request){
        studyGroupGoalService.saveGoalDetail(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/studygroup/goals/{goalId}/details")
    public ResponseEntity<List<StudyGroupGoalDetailResponse>> getStudyGroupGoalDetail(@PathVariable("goalId") Long goalId){
        List<StudyGroupGoalDetailResponse> studyGoalDetailList = studyGroupGoalService.getStudyGoalDetailList(goalId);
        return ResponseEntity.ok(studyGoalDetailList);
    }

    @DeleteMapping("/studygroup/goals/details/{detailGoalId}")
    public ResponseEntity<Void> deleteStudyGroupGoalDetail(@PathVariable("detailGoalId") Long detailGoalId){
        studyGroupGoalService.deleteGoalDetail(detailGoalId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/studygroup/goals/detail/{detailGoalId}/check")
    public ResponseEntity<Void> checkGoalDetail(@PathVariable("detailGoalId") Long detailGoalId, @RequestBody StudyGoalDetailCheckRequest check){
        studyGroupGoalService.checkGoalDetail(detailGoalId,check.getCheck());
        return ResponseEntity.ok().build();
    }
}
