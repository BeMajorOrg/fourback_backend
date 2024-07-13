package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> getMessagesByOauth2Id(String oauth2Id);
    void deleteMessagesByOauth2Id(String oauth2Id);
}
