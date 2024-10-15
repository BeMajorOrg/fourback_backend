package com.fourback.bemajor.domain.friendchat.service;

import com.fourback.bemajor.domain.chat.dto.ChatMessageResponseDto;
import com.fourback.bemajor.domain.friendchat.dto.FriendChatMessageResponseDto;
import com.fourback.bemajor.domain.friendchat.entity.FriendChatMessageEntity;
import com.fourback.bemajor.domain.friendchat.repository.FriendChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendChatMessageService {

    private final FriendChatMessageRepository friendChatMessageRepository;

    @Transactional
    public void saveMessage(Long receiverId, FriendChatMessageResponseDto chatMessageResponseDto) {
        FriendChatMessageEntity messageEntity = FriendChatMessageEntity.builder()
                .senderId(chatMessageResponseDto.getSenderId())
                .receiverId(receiverId)
                .senderName(chatMessageResponseDto.getSenderName())
                .message(chatMessageResponseDto.getContent())
                .sendTime(LocalDateTime.now())
                .build();
        friendChatMessageRepository.save(messageEntity);

    }

    public List<ChatMessageResponseDto> getMessages(Long senderId,Long receiverId) {
        List<FriendChatMessageEntity> messages = friendChatMessageRepository.findBySenderIdAndReceiverId(senderId, receiverId);
        return messages.stream()
                .map(FriendChatMessageEntity::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMessages(Long senderId, Long receiverId) {
        friendChatMessageRepository.deleteBySenderIdAndReceiverId(senderId, receiverId);
    }
}
