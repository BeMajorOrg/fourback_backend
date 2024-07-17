package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> getMessagesByOauth2Id(String oauth2Id);
    void deleteMessagesByOauth2Id(String oauth2Id);
}
