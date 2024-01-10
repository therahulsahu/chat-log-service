package com.example.chatlogservice.controller;

import com.example.chatlogservice.model.ChatLog;
import com.example.chatlogservice.service.ChatLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatlogs")
public class ChatLogController {
    @Autowired
    private ChatLogService chatLogService;

    @PostMapping("/{user}")
    public ResponseEntity<?> createChatLog(@PathVariable String user, @RequestBody ChatLog message) {
        String messageId = chatLogService.addChatLog(user, message);
        return new ResponseEntity<>(messageId, HttpStatus.CREATED);
    }

    @GetMapping("/{user}")
    public ResponseEntity<List<ChatLog>> getChatLogs(@PathVariable String user,
                                                     @RequestParam(defaultValue = "10") int limit,
                                                     @RequestParam(required = false) String start) {
        return new ResponseEntity<>(chatLogService.getChatLogs(user, limit, start), HttpStatus.OK);
    }

    @DeleteMapping("/{user}")
    public ResponseEntity<Void> deleteChatLogs(@PathVariable String user) {
        chatLogService.deleteChatLogs(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{user}/{msgId}")
    public ResponseEntity<Void> deleteChatLogMessage(@PathVariable String user, @PathVariable String msgId) {
        chatLogService.deleteChatLogs(user, msgId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
