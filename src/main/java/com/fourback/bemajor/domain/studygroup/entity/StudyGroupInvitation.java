package com.fourback.bemajor.domain.studygroup.entity;

import com.fourback.bemajor.domain.user.entity.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 스터디 그룹 초대 엔티티
 */
@Getter
@Entity
@NoArgsConstructor
public class StudyGroupInvitation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  @ManyToOne
  private StudyGroup studyGroup;
  @ManyToOne
  private UserEntity user;

  public StudyGroupInvitation(StudyGroup studyGroup, UserEntity user) {
    this.studyGroup = studyGroup;
    this.user = user;
  }

  /**
   * 스터디 그룹 초대 수락
   *
   * @return
   */
  public StudyJoined acceptInvitation() {
    return new StudyJoined(studyGroup, user);
  }
}
