package com.fourback.bemajor.domain.studygroup.service;

import com.fourback.bemajor.domain.studygroup.dto.OutgoingGroupChatMessageDto;
import com.fourback.bemajor.domain.studygroup.entity.GroupChatMessageEntity;
import com.fourback.bemajor.domain.studygroup.repository.GroupChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupChatMessageService {
    private final GroupChatMessageRepository groupChatMessageRepository;

    @Transactional
    @Async("threadPoolTaskExecutor")
    public void asyncSave(Long receiverId, Long studyGroupId, OutgoingGroupChatMessageDto outgoingMessageDto) {
        GroupChatMessageEntity message = GroupChatMessageEntity.of(receiverId, studyGroupId, outgoingMessageDto);
        groupChatMessageRepository.save(message);
    }

    public List<GroupChatMessageEntity> findAll(Long userId, Long studyGroupId) {
        return groupChatMessageRepository.findAllByReceiverIdAndStudyGroupId(userId, studyGroupId);
    }

    @Transactional
    public void deleteAll(Long userId, Long studyGroupId) {
        groupChatMessageRepository.deleteAllByReceiverIdAndStudyGroupId(userId, studyGroupId);
    }

    @Transactional
    public void deleteAllFromUser(Long userId) {
        groupChatMessageRepository.deleteAllByReceiverId(userId);
    }

    @Transactional
    public void deleteAllFromStudyGroup(Long studyGroupId){
        groupChatMessageRepository.deleteAllByStudyGroupId(studyGroupId);
    }
}