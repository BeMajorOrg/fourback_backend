package com.fourback.bemajor.domain.studygroup.controller;

import com.fourback.bemajor.domain.studygroup.dto.StudyGroupDto;
import com.fourback.bemajor.domain.studygroup.dto.response.*;
import com.fourback.bemajor.domain.studygroup.service.StudyGroupInvitationService;
import com.fourback.bemajor.domain.studygroup.service.StudyGroupService;
import com.fourback.bemajor.domain.studygroup.service.StudyJoinedService;
import com.fourback.bemajor.domain.user.dto.response.UserResponseDto;
import com.fourback.bemajor.global.common.util.ResponseUtil;
import com.fourback.bemajor.global.security.CustomUserDetails;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RestController
public class StudyGroupController {
  private final StudyGroupService studyGroupService;
  private final StudyJoinedService studyJoinedService;
  private final StudyGroupInvitationService studyGroupInvitationService;

  /**
   * 스터디 그룹 초대 수락
   *
   * @param invitationId
   * @return
   */
  @PostMapping("/studygroup/invitations/accept/{invitationId}")
  public ResponseEntity<Void> acceptInvitation(@PathVariable("invitationId") Long invitationId) {
    //TODO : 본인 확인
    studyGroupInvitationService.acceptInvitation(invitationId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/studygroup")
  public ResponseEntity<StudyGroupDto> createStudyGroup(@RequestBody StudyGroupDto studyGroupDto,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    studyGroupService.createStudyGroup(studyGroupDto, customUserDetails.getUserId());
    return ResponseEntity.ok(studyGroupDto);
  }

  @DeleteMapping("/studygroup/{studyGroupId}")
  public ResponseEntity<Void> deleteStudyGroup(@PathVariable("studyGroupId") Long studyGroupId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    studyGroupService.deleteStudyGroup(studyGroupId, customUserDetails.getUserId());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/studygroup/exitgroup/{studyGroupId}")
  public ResponseEntity<Void> exitGroup(@PathVariable("studyGroupId") Long groupId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    studyJoinedService.exitStudyGroup(groupId, customUserDetails.getUserId());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/studygroup/members/{studyGroupId}")
  public ResponseEntity<List<UserResponseDto>> getGroupMembers(@PathVariable("studyGroupId") Long studyGroupID,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    List<UserResponseDto> allStudyUser = studyJoinedService.getAllStudyUser(studyGroupID);
    return ResponseEntity.ok(allStudyUser);
  }

  /**
   * 받은 스터디 초대 개수
   *
   * @param customUserDetails
   * @return
   */
  @GetMapping("/studygroup/invitation/count")
  public ResponseEntity<StudyGroupInvitationCountResponse> getInvitationCount(
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    Integer invitationCount = studyGroupInvitationService.countInvitations(customUserDetails.getUserId());
    return ResponseEntity.ok(new StudyGroupInvitationCountResponse(invitationCount));
  }

  /**
   * 받은 초대 확인
   *
   * @param customUserDetails
   * @return
   */
  @GetMapping("/studygroup/invitations")
  public ResponseEntity<List<StudyGroupInvitationResponse>> getInvitations(
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    List<StudyGroupInvitationResponse> invitations =
        studyGroupInvitationService.getInvitations(customUserDetails.getUserId());
    return ResponseEntity.ok(invitations);
  }

  /**
   * 이메일로 유저 정보 받기
   *
   * @param email
   * @param customUserDetails
   * @return
   */
  @GetMapping("/studygroup/users/{email}")
  public ResponseEntity<StudyMemberResponse> getMemberByEmail(@PathVariable("email") String email,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    StudyMemberResponse userInfoByEmail = studyGroupInvitationService.getUserInfoByEmail(email);
    return ResponseEntity.ok(userInfoByEmail);
  }

  @GetMapping("/studygroup")
  public List<StudyGroupDto> getStudyGroup(@RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "category", defaultValue = "") String category) {
    return studyGroupService.getAllStudyGroup(page, category);
  }

  /**
   * 유저 스터디그룹에 초대
   *
   * @param studyGroupId
   * @param userId
   * @return
   */
  @PostMapping("/studygroup/{studyGroupId}/invitations/{userId}")
  public ResponseEntity<Void> inviteUserToStudyGroup(@PathVariable("studyGroupId") Long studyGroupId,
      @PathVariable("userId") Long userId) {
    studyGroupInvitationService.sendInvitation(studyGroupId, userId);
    return ResponseEntity.ok().build();
  }

  /**
   * 스터디 그룹 참가 신청
   * @param groupId
   * @param customUserDetails
   * @return
   */
  @PostMapping("/studygroup/joingroup/{studyGroupId}")
  public ResponseEntity<Void> joinGroup(@PathVariable("studyGroupId") Long groupId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    studyJoinedService.joinStudyGroup(customUserDetails.getUserId(), groupId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/studygroup/mygroups")
  public ResponseEntity<List<StudyGroupDto>> myStudyGroupList(
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    List<StudyGroupDto> allMyGroups = studyJoinedService.getAllMyGroups(customUserDetails.getUserId());
    return ResponseEntity.ok(allMyGroups);
  }

  @PutMapping("/studygroup/{studyGroupId}")
  public ResponseEntity<StudyGroupDto> updateStudyGroup(@PathVariable("studyGroupId") Long studyGroupId,
      @RequestBody StudyGroupDto studyGroupDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    studyGroupService.updateStudyGroup(studyGroupId, studyGroupDto, customUserDetails.getUserId());
    return ResponseEntity.ok(studyGroupDto);
  }

  /**
   * 스터디 그룹과 어떤 관계인지 -> NONE, MEMBER, ADMIN
   * @param studyGroupId
   * @param customUserDetails
   * @return
   */
  @GetMapping("/studygroup/{studyGroupId}/role")
  public ResponseEntity<StudyGroupRoleResponse> getRoleOfGroup(@PathVariable("studyGroupId") Long studyGroupId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
    StudyGroupRoleResponse role = studyJoinedService.getRole(customUserDetails.getUserId(), studyGroupId);
    return ResponseEntity.ok(role);
  }

  /**
   * 받은 스터디 그룹 참여 신청 리스트 조회
   * @param studyGroupId
   * @param customUserDetails
   * @return
   */
  @GetMapping("/studygroup/applications/{studyGroupId}")
  public ResponseEntity<List<StudyGroupApplicationResponse>> getApplications(@PathVariable("studyGroupId") Long studyGroupId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
    List<StudyGroupApplicationResponse> applications = studyJoinedService.getApplications(customUserDetails.getUserId(), studyGroupId);
    return ResponseEntity.ok(applications);
  }

  /**
   * 스터디 그룹 참여 신청 수락
   * @param studyApplicationId
   * @param customUserDetails
   * @return
   */
  @PostMapping("/studygroup/applications/{studyApplicationId}/accept")
  public ResponseEntity<Void> acceptApplications(@PathVariable("studyApplicationId") Long studyApplicationId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
    studyJoinedService.authorizeStudyGroupApplication(studyApplicationId);
    return ResponseEntity.ok().build();
  }

  /**
   * 스터디 그룹 입장 신청 개수 조회
   * @param studyGroupId
   * @param customUserDetails
   * @return
   */
  @GetMapping("/studygroup/applications/{studyGroupId}/count")
  public ResponseEntity<StudyGroupApplicationCountResponse> getApplicationCount(@PathVariable("studyGroupId") Long studyGroupId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    StudyGroupApplicationCountResponse applicationCount = studyJoinedService.getApplicationCount(customUserDetails.getUserId(), studyGroupId);
    return ResponseEntity.ok(applicationCount);
  }

  @PostMapping("/studygroup/{studyGroupId}/notification")
  public ResponseEntity<?> onNotification(@PathVariable("studyGroupId") Long studyGroupId,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {

    return ResponseUtil.onSuccess();
  }
}
