package com.fourback.bemajor.domain.studygroup.service;

import com.fourback.bemajor.domain.studygroup.dto.OutgoingGroupChatMessageDto;
import com.fourback.bemajor.domain.studygroup.entity.GroupChatMessageEntity;
import com.fourback.bemajor.domain.studygroup.repository.GroupChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupChatMessageService {
    private final GroupChatMessageRepository groupChatMessageRepository;

    @Transactional
    @Async("threadPoolTaskExecutor")
    public void asyncSave(Long receiverId, Long studyGroupId, OutgoingGroupChatMessageDto outgoingMessageDto) {
        GroupChatMessageEntity message = outgoingMessageDto.toMessageEntity(receiverId, studyGroupId);
        groupChatMessageRepository.save(message);
    }

    @Transactional
    public void deleteAll(Long userId, Long studyGroupId) {
        groupChatMessageRepository.deleteByReceiverIdAndStudyGroupId(userId, studyGroupId);
    }

    @Transactional
    public void deleteAll(Long userId) {
        groupChatMessageRepository.deleteMessagesByReceiverId(userId);
    }
}