package com.project.demo.rest.chat;

import com.project.demo.logic.entity.chat.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/stream/chats")
@RestController
public class ChatController {
    @Autowired
    private ChatService chatService;
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{chatId}")
    public ResponseEntity<Map<String, Object>> getChatById(@PathVariable Long chatId) {
        Chat chat = chatService.findById(chatId);
        if (chat == null) {
            return ResponseEntity.notFound().build();
        }
        String messagesContent = chat.getMessages().stream()
                .map(Message::getContent)
                .collect(Collectors.joining(" "));
        Map<String, Object> response = new HashMap<>();
        response.put("id", chat.getId());
        response.put("messages", messagesContent);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Chat> createChat(@RequestBody Chat chat) {
        Chat savedChat = chatService.save(chat);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedChat);
    }
}
