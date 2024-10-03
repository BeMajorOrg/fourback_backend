package com.fourback.bemajor.domain.studygroup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 스터디 그룹 초대 개수
 */
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class StudyGroupInvitationCountResponse {
  private Integer invitationCount;
}
