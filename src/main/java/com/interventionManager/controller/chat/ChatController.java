package com.interventionManager.controller.chat;

import com.interventionManager.entities.Chat;
import com.interventionManager.services.chat.ChatServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@CrossOrigin("*")
public class ChatController {
    private final ChatServiceImpl chatMessageService;

    @PostMapping("/save")
    public Chat saveMessage(@RequestBody Chat chatMessage) {
        return chatMessageService.saveMessage(chatMessage);
    }

    @GetMapping("/messages/{technicianId}")
    public ResponseEntity<List<Chat>> getMessages(@PathVariable String technicianId) {
        List<Chat> messages = chatMessageService.getRecentMessages(technicianId);
        return ResponseEntity.ok(messages);
    }
}
