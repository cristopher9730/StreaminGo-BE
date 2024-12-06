package com.project.demo.rest.chat;

import com.project.demo.logic.entity.chat.Chat;
import com.project.demo.logic.entity.chat.ChatService;
import com.project.demo.logic.entity.chat.Message;
import com.project.demo.logic.entity.chat.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stream/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatService chatService;

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message savedMessage = messageService.save(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }
}
