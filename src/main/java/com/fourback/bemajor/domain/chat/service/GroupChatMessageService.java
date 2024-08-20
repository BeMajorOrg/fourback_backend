package com.fourback.bemajor.domain.chat.service;

import com.fourback.bemajor.domain.chat.dto.ChatMessageDto;
import com.fourback.bemajor.domain.chat.entitiy.GroupChatMessageEntity;
import com.fourback.bemajor.domain.chat.repository.GroupChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupChatMessageService {
    private final GroupChatMessageRepository groupChatMessageRepository;

    public List<ChatMessageDto> getMessages(Long userId, Long studyGroupId) {
        List<GroupChatMessageEntity> groupChatMessageEntities = groupChatMessageRepository
                .findByStudyGroupIdAndReceiverId(userId, studyGroupId);
        return groupChatMessageEntities.stream()
                .map(GroupChatMessageEntity::toMessageDto).toList();
    }

    @Transactional
    public void deleteMessages(Long userId, Long studyGroupId) {
        groupChatMessageRepository.deleteMessagesByStudyGroupIdAndReceiverId(userId, studyGroupId);
    }

    @Transactional
    public void saveMessage(Long userId, ChatMessageDto chatMessageDto, Long studyGroupId) {
        groupChatMessageRepository.save(chatMessageDto.toMessageEntity(userId, studyGroupId));
    }
}