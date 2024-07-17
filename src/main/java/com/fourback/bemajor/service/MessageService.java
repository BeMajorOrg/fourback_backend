package com.fourback.bemajor.service;

import com.fourback.bemajor.domain.ChatMessage;
import com.fourback.bemajor.dto.ChatMessageDto;
import com.fourback.bemajor.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    @Transactional
    public List<ChatMessageDto> getMessages(String oauth2Id) {
        List<ChatMessage> chatMessages = messageRepository.getMessagesByOauth2Id(oauth2Id);
        return chatMessages.stream().map(ChatMessage::toMessageDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteMessagesByOauth2Id(String oauth2Id) {
        messageRepository.deleteMessagesByOauth2Id(oauth2Id);
    }

    @Transactional
    public void saveMessageByOauth2Id(String oauth2Id, ChatMessageDto chatMessageDto) {
        messageRepository.save(chatMessageDto.toMessageEntity(oauth2Id));
    }
}
