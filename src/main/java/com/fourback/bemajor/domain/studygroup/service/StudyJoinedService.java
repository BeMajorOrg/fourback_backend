package com.fourback.bemajor.domain.studygroup.service;

import com.fourback.bemajor.domain.chat.service.GroupChatMessageService;
import com.fourback.bemajor.domain.studygroup.dto.StudyGroupDto;
import com.fourback.bemajor.domain.studygroup.dto.request.StudyGroupAlarmDto;
import com.fourback.bemajor.domain.studygroup.dto.response.StudyGroupApplicationCountResponse;
import com.fourback.bemajor.domain.studygroup.dto.response.StudyGroupApplicationResponse;
import com.fourback.bemajor.domain.studygroup.dto.response.StudyGroupDetailsResponseDto;
import com.fourback.bemajor.domain.studygroup.dto.response.StudyGroupRoleResponse;
import com.fourback.bemajor.domain.studygroup.entity.StudyGroup;
import com.fourback.bemajor.domain.studygroup.entity.StudyJoinApplication;
import com.fourback.bemajor.domain.studygroup.entity.StudyJoined;
import com.fourback.bemajor.domain.studygroup.repository.StudyGroupRepository;
import com.fourback.bemajor.domain.studygroup.repository.StudyJoinApplicationRepository;
import com.fourback.bemajor.domain.studygroup.repository.StudyJoinedRepository;
import com.fourback.bemajor.domain.user.dto.response.UserResponseDto;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import com.fourback.bemajor.global.common.enums.RedisKeyPrefixEnum;
import com.fourback.bemajor.global.common.service.RedisService;
import com.fourback.bemajor.global.common.enums.RedisKeyPrefixEnum;
import com.fourback.bemajor.global.common.service.FcmService;
import com.fourback.bemajor.global.common.service.RedisService;
import com.fourback.bemajor.global.exception.kind.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyJoinedService {
    private final StudyJoinedRepository studyJoinedRepository;
    private final UserRepository userRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final GroupChatMessageService groupChatMessageService;
    private final StudyJoinApplicationRepository studyJoinApplicationRepository;
    private final RedisService redisService;
    private final Map<Long, Set<WebSocketSession>> websocketSessionsMap;
    private final FcmService fcmService;

    /**
     * 스터디 그룹 참여 신청
     * @param userId
     * @param studyGroupId
     */
    public void joinStudyGroup(Long userId, Long studyGroupId) {
        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId).orElseThrow(() -> new NotFoundException("no such study group."));
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("no such user."));

        //TODO - 스터디 그룹 이미 참여하고 있는지 검사

        //TODO - 스터디 그룹 이미 지원했는지 검사

        studyJoinApplicationRepository.save(new StudyJoinApplication(userEntity, studyGroup));

        Long ownerUserId = studyGroup.getOwnerUserId();
        String fcmToken = redisService.getValue(RedisKeyPrefixEnum.FCM, ownerUserId);
        fcmService.sendStudyGroupAlarm(StudyGroupAlarmDto.builder()
                        .title(studyGroup.getStudyName())
                        .fcmToken(fcmToken)
                .message(userEntity.getUserName() + "님이 그룹 참여를 신청하셨습니다.")
                .build());

    }

    /**
     * 스터디 그룹 들어오기 수락
     * @param studyJoinApplicationId
     */
    @Transactional
    public void authorizeStudyGroupApplication(Long studyJoinApplicationId){
        //TODO - 유저가 어드민인지 확인
        StudyJoinApplication studyJoinApplication = studyJoinApplicationRepository.findById(studyJoinApplicationId).orElseThrow(() -> new RuntimeException("TODO - 예외처리"));
        StudyGroup studyGroup = studyJoinApplication.getStudyGroup();
        UserEntity user = studyJoinApplication.getUser();
        studyJoinedRepository.save(new StudyJoined(studyGroup,user, true));
        Long studyGroupId = studyGroup.getId();
        Long userId = user.getUserId();
        putDisConnectedUser(studyGroupId, userId);
        studyJoinApplicationRepository.deleteById(studyJoinApplicationId);

        String fcmToken = redisService.getValue(RedisKeyPrefixEnum.FCM, user.getUserId());
        fcmService.sendStudyGroupAlarm(StudyGroupAlarmDto.builder()
                .fcmToken(fcmToken)
                .title(studyGroup.getStudyName())
                .message(studyGroup.getStudyName() + "그룹 입장신청이 수락되었습니다.").build());
    }

    /**
     * 유저와 스터디 그룹과의 관계 조회
     * @param userId
     * @param studyGroupId
     * @return
     */
    public StudyGroupRoleResponse getRole(Long userId, Long studyGroupId){
        Optional<StudyJoined> joined = studyJoinedRepository.findByUser_UserIdAndStudyGroup_Id(userId, studyGroupId);
        if (joined.isEmpty()){ //참여 안한 관계
            return new StudyGroupRoleResponse("NONE");
        }
        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId).orElseThrow(() -> new NotFoundException("no such study group."));
        if (studyGroup.getOwnerUserId().equals(userId)){
            return new StudyGroupRoleResponse("ADMIN");
        }
        return new StudyGroupRoleResponse("MEMBER");
    }

    /**
     * 참여 지원 목록 조회
     * @param userId
     * @param studyGroupId
     * @return
     */
    public List<StudyGroupApplicationResponse> getApplications(Long userId, Long studyGroupId){
        //TODO - 유저가 어드민인지 확인
        List<StudyJoinApplication> allByStudyGroupId = studyJoinApplicationRepository.findAllByStudyGroup_Id(studyGroupId);
        return allByStudyGroupId.stream().map(StudyGroupApplicationResponse::fromEntity).collect(Collectors.toList());
    }

    /**
     * 받은 그룹 참여 신청서 개수 조회
     * @param userId
     * @param studyGroupId
     * @return
     */
    public StudyGroupApplicationCountResponse getApplicationCount(Long userId, Long studyGroupId){
        //TODO - 유저가 어드민인지 확인
        Long count = studyJoinApplicationRepository.countByStudyGroup_Id(studyGroupId);
        return new StudyGroupApplicationCountResponse(count);
    }


    @Transactional
    public void exitStudyGroup(Long studyGroupId, Long userId) {
        List<Long> idsByStudyGroupIdAndOauth2Id = studyJoinedRepository.findIdsByStudyGroupIdAndOauth2Id(studyGroupId, userId);
        studyJoinedRepository.deleteByIds(idsByStudyGroupIdAndOauth2Id);
        if (!websocketSessionsMap.get(studyGroupId).isEmpty()) {
            redisService.deleteLongBooleanField(RedisKeyPrefixEnum.DISCONNECTED, studyGroupId, userId);
        }
        groupChatMessageService.deleteMessages(userId, studyGroupId);
    }

    public StudyGroupDetailsResponseDto getDetails(Long studyGroupId, Long userId) {
        List<StudyJoined> studyJoined = studyJoinedRepository.findByStudyGroupId(studyGroupId);
        List<UserResponseDto> userResponses = studyJoined.stream().map(StudyJoined::getUser)
                .map(UserEntity::toUserResponseDto).toList();
        Boolean isAlarmSet = studyJoined.stream().filter(joined -> joined.getUser().getUserId().equals(userId))
                .map(StudyJoined::getIsAlarmSet).findFirst().orElse(false);
        return StudyGroupDetailsResponseDto.of(userResponses, isAlarmSet);
    }

    public List<StudyGroupDto> getAllMyGroups(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("no such user.");
        }
        return userOptional.get().getStudyJoineds().stream().map(StudyJoined::getStudyGroup).map(StudyGroupDto::toDto).collect(Collectors.toList());
    }

    public void update(Long studyGroupId, Long userId) {
        StudyJoined studyJoined = studyJoinedRepository
                .findByUser_UserIdAndStudyGroup_Id(userId, studyGroupId)
                .orElseThrow(() -> new NotFoundException("no such study joined."));
        Boolean isAlarmSet = studyJoined.getIsAlarmSet();
        studyJoined.changeAlarmSet(!isAlarmSet);
        studyJoinedRepository.save(studyJoined);
        if(!websocketSessionsMap.get(studyGroupId).isEmpty()){
            redisService.putLongBooleanField(RedisKeyPrefixEnum.DISCONNECTED,
                    studyGroupId, userId, !isAlarmSet);
        }
    }

    protected void putDisConnectedUser(Long studyGroupId, Long userId) {
        if(!websocketSessionsMap.get(studyGroupId).isEmpty()){
            redisService.putLongBooleanField(RedisKeyPrefixEnum.DISCONNECTED, studyGroupId, userId, true);
        }
    }
}
