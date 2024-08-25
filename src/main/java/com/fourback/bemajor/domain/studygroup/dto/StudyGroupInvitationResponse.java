package com.fourback.bemajor.domain.studygroup.dto;

import com.fourback.bemajor.domain.studygroup.entity.StudyGroupInvitation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 스터디 그룹 초대 응답
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudyGroupInvitationResponse {
  private Long invitationId;
  private String studyName;
  private String category;
  private String studyLocation;
  private String studyCycle;

  public static StudyGroupInvitationResponse from(StudyGroupInvitation invitation) {
    return new StudyGroupInvitationResponse(invitation.getId(), invitation.getStudyGroup().getStudyName(),
        invitation.getStudyGroup().getCategory(), invitation.getStudyGroup().getStudyLocation(),
        invitation.getStudyGroup().getStudyCycle());
  }
}
