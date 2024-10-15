package com.fourback.bemajor.domain.studygroup.service;

import com.fourback.bemajor.domain.studyGroupNotification.service.StudyGroupNotificationService;
import com.fourback.bemajor.domain.studygroup.dto.response.StudyGroupInvitationResponse;
import com.fourback.bemajor.domain.studygroup.dto.response.StudyMemberResponse;
import com.fourback.bemajor.domain.studygroup.entity.StudyGroup;
import com.fourback.bemajor.domain.studygroup.entity.StudyGroupInvitation;
import com.fourback.bemajor.domain.studygroup.entity.StudyJoined;
import com.fourback.bemajor.domain.studygroup.repository.StudyGroupInvitationRepository;
import com.fourback.bemajor.domain.studygroup.repository.StudyGroupRepository;
import com.fourback.bemajor.domain.studygroup.repository.StudyJoinedRepository;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 스터디 그룹 초대 서비스
 */
//TODO : 나중에 예외 처리 제대로
@Service
@RequiredArgsConstructor
public class StudyGroupInvitationService {
  private final StudyGroupInvitationRepository studyGroupInvitationRepository;
  private final StudyGroupRepository studyGroupRepository;
  private final StudyJoinedRepository studyJoinedRepository;
  private final UserRepository userRepository;
  private final StudyGroupNotificationService studyGroupNotificationService;

  /**
   * 스터디 초대 수락
   *
   * @param invitationId
   */
  @Transactional
  public void acceptInvitation(Long invitationId) {
    StudyGroupInvitation studyGroupInvitation =
        studyGroupInvitationRepository.findById(invitationId).orElseThrow(RuntimeException::new);
    StudyJoined studyJoined = studyGroupInvitation.acceptInvitation();
    studyJoinedRepository.save(studyJoined);
    studyGroupInvitationRepository.delete(studyGroupInvitation);
    studyGroupNotificationService.enableRealTimeNotification(
            studyJoined.getStudyGroup().getId(),studyJoined.getUser().getUserId());

  }

  @Transactional(readOnly = true)
  public Integer countInvitations(Long userId) {
    return studyGroupInvitationRepository.countAllByUser_UserId(userId);
  }

  //TODO : 나중에 쿼리 개수 최적화

  /**
   * 받은 초대들 확인하기
   *
   * @param userId
   * @return
   */
  public List<StudyGroupInvitationResponse> getInvitations(Long userId) {
    List<StudyGroupInvitation> invitations = studyGroupInvitationRepository.findAllByUser_UserId(userId);
    return invitations.stream().map(StudyGroupInvitationResponse::from).toList();
  }

  /**
   * 이메일로 유저 정보 찾기
   *
   * @param email
   * @return
   */
  @Transactional(readOnly = true)
  public StudyMemberResponse getUserInfoByEmail(String email) {
    UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    return StudyMemberResponse.fromUserEntity(userEntity);
  }

  /**
   * 스터디 초대 발송
   *
   * @param groupId
   * @param to
   */
  @Transactional
  public void sendInvitation(Long groupId, Long to) {
    UserEntity userEntity = userRepository.findById(to).orElseThrow(RuntimeException::new);
    StudyGroup studyGroup = studyGroupRepository.findById(groupId).orElseThrow(RuntimeException::new);
    //TODO : 중복 참여 방지 로직 추가
    studyGroupInvitationRepository.save(new StudyGroupInvitation(studyGroup, userEntity));

  }
}
