package com.fourback.bemajor.service;

import com.fourback.bemajor.domain.Message;
import com.fourback.bemajor.dto.MessageDto;
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
    public List<MessageDto> getMessages(String oauth2Id) {
        List<Message> messages = messageRepository.getMessagesByOauth2Id(oauth2Id);
        return messages.stream().map(Message::toMessageDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteMessagesByOauth2Id(String oauth2Id) {
        messageRepository.deleteMessagesByOauth2Id(oauth2Id);
    }

    @Transactional
    public void saveMessageByOauth2Id(String oauth2Id, MessageDto messageDto) {
        messageRepository.save(messageDto.toMessageEntity(oauth2Id));
    }
}
