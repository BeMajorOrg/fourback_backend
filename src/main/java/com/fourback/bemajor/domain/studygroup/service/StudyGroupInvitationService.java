package com.fourback.bemajor.domain.studygroup.service;

import com.fourback.bemajor.domain.studygroup.dto.request.StudyGroupAlarmDto;
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
import com.fourback.bemajor.global.common.enums.FieldKeyEnum;
import com.fourback.bemajor.global.common.enums.KeyPrefixEnum;
import com.fourback.bemajor.global.common.service.FcmService;
import com.fourback.bemajor.global.common.service.RedisService;
import com.fourback.bemajor.global.exception.kind.NoSpaceException;
import com.fourback.bemajor.global.exception.kind.NotFoundException;
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
  private final StudyJoinedService studyJoinedService;
  private final FcmService fcmService;
  private final RedisService redisService;

  /**
   * 스터디 초대 수락
   *
   * @param invitationId
   */
  @Transactional
  public void acceptInvitation(Long invitationId) {
    StudyGroupInvitation studyGroupInvitation =
        studyGroupInvitationRepository.findById(invitationId).orElseThrow(RuntimeException::new);
    StudyGroup studyGroup = studyGroupInvitation.getStudyGroup();
    Integer joinedCount = studyJoinedRepository.countByStudyGroup_Id(studyGroup.getId());
    if (studyGroup.getTeamSize() <= joinedCount) throw new NoSpaceException("이미 가득찬 스터디 그룹입니다.");

    StudyJoined studyJoined = studyGroupInvitation.acceptInvitation();
    studyJoinedRepository.save(studyJoined);
    studyGroupInvitationRepository.delete(studyGroupInvitation);

    Long studyGroupId = studyJoined.getStudyGroup().getId();
    Long userId = studyJoined.getUser().getId();

    studyJoinedService.putDisConnectedUserIfActiveChat(userId, studyGroupId);

    Long ownerUserId = studyJoined.getStudyGroup().getOwnerUserId();
    UserEntity userEntity = userRepository.findById(ownerUserId)
            .orElseThrow(() -> new NotFoundException("방장을 찾을 수 없는 그룹입니다."));

    String fcmToken = redisService.getFieldValue(
        KeyPrefixEnum.TOKENS.getKeyPrefix() + userEntity.getId(), FieldKeyEnum.FCM.getFieldKey());

    if (fcmToken == null) return;

    fcmService.sendStudyGroupAlarm(StudyGroupAlarmDto.builder()
            .title(studyJoined.getStudyGroup().getStudyName())
            .fcmToken(fcmToken)
            .message(userEntity.getUserName() + "님이 그룹 초대를 수락하셨습니다.").build());
  }

  @Transactional(readOnly = true)
  public Integer countInvitations(Long userId) {
    return studyGroupInvitationRepository.countAllByUser_Id(userId);
  }

  //TODO : 나중에 쿼리 개수 최적화

  /**
   * 받은 초대들 확인하기
   *
   * @param userId
   * @return
   */
  public List<StudyGroupInvitationResponse> getInvitations(Long userId) {
    List<StudyGroupInvitation> invitations = studyGroupInvitationRepository.findAllByUser_Id(userId);
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

    String fcmToken = redisService.getFieldValue(
        KeyPrefixEnum.TOKENS.getKeyPrefix() + userEntity.getId(), FieldKeyEnum.FCM.getFieldKey());
    fcmService.sendStudyGroupAlarm(StudyGroupAlarmDto.builder()
                    .fcmToken(fcmToken)
                    .title(studyGroup.getStudyName())
                    .message(studyGroup.getStudyName() + "그룹에게서 초대장이 도착했습니다.")
            .build());
  }
}
