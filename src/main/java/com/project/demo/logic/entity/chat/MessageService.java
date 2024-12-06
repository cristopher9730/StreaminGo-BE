package com.project.demo.logic.entity.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    public List<Message> findByChat(Chat chat){
        return messageRepository.findByChat(chat);
    }

    public Message save(Message message){
        // Find the chat by ID
        Chat chat = chatRepository.findById(message.getChat().getId()).orElseThrow(() -> new RuntimeException("Chat not found"));
        message.setChat(chat);
        return messageRepository.save(message);
    }
}
