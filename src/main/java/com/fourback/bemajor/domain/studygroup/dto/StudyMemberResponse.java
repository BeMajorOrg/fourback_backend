package com.fourback.bemajor.domain.studygroup.dto;

import com.fourback.bemajor.domain.user.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 스터디 멤버 정보 응답
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudyMemberResponse {
  private Long userId;
  private String name;
  private String profileImage;

  public static StudyMemberResponse fromUserEntity(UserEntity userEntity) {
    System.out.println(userEntity);
    return new StudyMemberResponse(userEntity.getUserId(), userEntity.getUserName(), userEntity.getFileName());
  }
}
