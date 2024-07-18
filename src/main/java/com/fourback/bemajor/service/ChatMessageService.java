package com.fourback.bemajor.service;

import com.fourback.bemajor.domain.ChatMessage;
import com.fourback.bemajor.dto.ChatMessageDto;
import com.fourback.bemajor.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public List<ChatMessageDto> getMessages(String oauth2Id,long studyGroupId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByStudyGroupIdAndOauth2Id(studyGroupId,oauth2Id);
        return chatMessages.stream().map(ChatMessage::toMessageDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteMessages(String oauth2Id, long studyGroupId) {
        chatMessageRepository.deleteMessagesByStudyGroupIdAndOauth2Id(studyGroupId,oauth2Id);
    }

    @Transactional
    public void deleteMessages(String oauth2Id) {
        chatMessageRepository.deleteMessagesByOauth2Id(oauth2Id);
    }

    @Transactional
    public void saveMessage(String oauth2Id, ChatMessageDto chatMessageDto, long studyGroupId) {
        chatMessageRepository.save(chatMessageDto.toMessageEntity(oauth2Id,studyGroupId));
    }
}
