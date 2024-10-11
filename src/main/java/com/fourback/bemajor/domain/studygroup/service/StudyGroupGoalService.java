package com.fourback.bemajor.domain.studygroup.service;

import com.fourback.bemajor.domain.studygroup.dto.request.StudyGroupGoalCreateRequest;
import com.fourback.bemajor.domain.studygroup.dto.request.StudyGroupGoalDetailCreateRequest;
import com.fourback.bemajor.domain.studygroup.dto.request.StudyGroupGoalUpdateRequest;
import com.fourback.bemajor.domain.studygroup.dto.response.StudyGroupGoalDetailResponse;
import com.fourback.bemajor.domain.studygroup.dto.response.StudyGroupGoalResponse;
import com.fourback.bemajor.domain.studygroup.entity.StudyDetailGoal;
import com.fourback.bemajor.domain.studygroup.entity.StudyGoal;
import com.fourback.bemajor.domain.studygroup.entity.StudyGroup;
import com.fourback.bemajor.domain.studygroup.repository.StudyDetailGoalRepository;
import com.fourback.bemajor.domain.studygroup.repository.StudyGoalRepository;
import com.fourback.bemajor.domain.studygroup.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 스터디 목표 서비스
 */
@Service
@RequiredArgsConstructor
public class StudyGroupGoalService {
    private final StudyGoalRepository studyGoalRepository;
    private final StudyDetailGoalRepository studyDetailGoalRepository;
    private final StudyGroupRepository studyGroupRepository;

    /**
     * 스터디 그룹 목표 가져오기
     * @param studyGroupId
     * @return 목표 리스트
     */
    @Transactional(readOnly = true)
    public List<StudyGroupGoalResponse> getStudyGroupGoals(Long studyGroupId){
        List<StudyGoal> allByStudyGroupId = studyGoalRepository.findAllByStudyGroupId(studyGroupId);
        return allByStudyGroupId.stream().map(StudyGroupGoalResponse::fromEntity).toList();
    }

    /**
     * 스터디 그룹 목표 생성
     * @param studyGroupGoalCreateRequest
     */
    @Transactional
    public void saveGoal(StudyGroupGoalCreateRequest studyGroupGoalCreateRequest){
        //TODO - 스터디 그룹 멤버 인증
        Long studyGroupId = studyGroupGoalCreateRequest.getStudyGroupId();
        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId).orElseThrow(() -> new RuntimeException("존재하지 않는 그룹"));
        StudyGoal studyGoal = new StudyGoal(studyGroupGoalCreateRequest.getGoalName(), studyGroupGoalCreateRequest.getEndDate(), studyGroup);
        studyGoalRepository.save(studyGoal);
    }

    /**
     * 스터디 그룹 목표 업데이트
     * @param studyGroupGoalUpdateRequest
     */
    @Transactional
    public void updateGoal(StudyGroupGoalUpdateRequest studyGroupGoalUpdateRequest){
        //TODO - 스터디 그룹 멤버 인증
        StudyGoal goal = studyGoalRepository.findById(studyGroupGoalUpdateRequest.getGoalId()).orElseThrow(() -> new RuntimeException("존재하지 않는 그룹"));
        goal.update(studyGroupGoalUpdateRequest);
    }

    /**
     * 스터디 그룹 목표 삭제
     * @param goalId
     */
    @Transactional
    public void deleteGoal(Long goalId){
        //TODO - 스터디 그룹 멤버 인증
        studyGoalRepository.deleteById(goalId);
    }

    /**
     * 스터디 그룹 세부 목표 생성
     * @param request
     */
    @Transactional
    public void saveGoalDetail(StudyGroupGoalDetailCreateRequest request){
        StudyGoal studyGoal = studyGoalRepository.findById(request.getStudyGroupGoalId()).orElseThrow(() -> new RuntimeException("존재하지 않는 그룹"));
        studyDetailGoalRepository.save(new StudyDetailGoal(request.getDetailGoalName(), studyGoal));
    }

    @Transactional(readOnly = true)
    public List<StudyGroupGoalDetailResponse> getStudyGoalDetailList(Long goalId){
        List<StudyDetailGoal> detailGoals = studyDetailGoalRepository.findAllByStudyGoal_Id(goalId);
        return detailGoals.stream().map(StudyGroupGoalDetailResponse::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public void deleteGoalDetail(Long detailGoalId){
        studyDetailGoalRepository.deleteById(detailGoalId);
    }

    @Transactional
    public void checkGoalDetail(Long detailGoalId,Boolean check){
        StudyDetailGoal detailGoal = studyDetailGoalRepository.findById(detailGoalId).orElseThrow(() -> new RuntimeException("존재하지 않는 세부목표"));
        detailGoal.checkValid(check);
    }
}
