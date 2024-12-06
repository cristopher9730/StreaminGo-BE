package com.project.demo.logic.entity.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;

    public Chat findById(Long id) {
        return chatRepository.findById(id).orElse(null);
    }

    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }
}
