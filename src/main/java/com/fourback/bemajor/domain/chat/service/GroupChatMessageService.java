package com.fourback.bemajor.domain.chat.service;

import com.fourback.bemajor.domain.chat.dto.ChatMessageResponseDto;
import com.fourback.bemajor.domain.chat.entitiy.GroupChatMessageEntity;
import com.fourback.bemajor.domain.chat.repository.GroupChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupChatMessageService {
    private final GroupChatMessageRepository groupChatMessageRepository;

    public List<ChatMessageResponseDto> getMessages(Long userId, Long studyGroupId) {
        List<GroupChatMessageEntity> groupChatMessageEntities = groupChatMessageRepository
                .findByReceiverIdAndStudyGroupId(userId, studyGroupId);
        return groupChatMessageEntities.stream()
                .map(GroupChatMessageEntity::toMessageResponseDto).toList();
    }

    @Transactional
    public void deleteAll(Long userId, Long studyGroupId) {
        groupChatMessageRepository.deleteByReceiverIdAndStudyGroupId(userId, studyGroupId);
    }

    @Transactional
    public void deleteAll(Long userId){
        groupChatMessageRepository.deleteMessagesByReceiverId(userId);
    }

    @Transactional
    @Async("threadPoolTaskExecutor")
    public void saveMessage(Long userId, ChatMessageResponseDto chatMessageDto, Long studyGroupId) {
        groupChatMessageRepository.save(chatMessageDto.toMessageEntity(userId, studyGroupId));
    }
}