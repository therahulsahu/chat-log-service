package com.example.chatlogservice.service.impl;

import com.example.chatlogservice.model.ChatLog;
import com.example.chatlogservice.service.ChatLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatLogServiceImpl implements ChatLogService {

    private final Map<String, List<ChatLog>> chatLogs = new HashMap<>();

    public String addChatLog(String user, ChatLog message) {
//        validating user length
        validateUserLength(user);
        log.debug("{}", chatLogs);

        String messageId = UUID.randomUUID().toString();  // Simulate a message ID
        message.setId(messageId);
        chatLogs.computeIfAbsent(user, k -> new ArrayList<>()).add(message);

        log.debug("{}", chatLogs);

        return message.getId();
    }

    public List<ChatLog> getChatLogs(String user, int limit, String start) {

        validateUserLength(user);

        List<ChatLog> logs = chatLogs.getOrDefault(user, Collections.emptyList());
        ArrayList<ChatLog> reversed = new ArrayList<>(logs);
        Collections.reverse(reversed);

        log.debug("{}", logs);

        // Find the index of the message with the start ID
        int startIndex = -1;
        if (start != null && !start.isEmpty()) {
            for (int i = reversed.size()-1; i >=0;i--) {
                if (reversed.get(i).getId().equals(start)) {
                    startIndex = i;
                    break;
                }
            }
        }

        // If start ID is found, adjust the list; otherwise return the top 'limit' messages
        List<ChatLog> result;
        if (startIndex != -1) {
            result = reversed.subList(startIndex, reversed.size());
        } else {
            result = reversed;
        }
        return result.stream().limit(limit).collect(Collectors.toList());
    }

    public void deleteChatLogs(String user) {
        validateUser(user);
        log.debug("{}", chatLogs);

        chatLogs.remove(user);
    }

    public void deleteChatLogs(String user, String msgId) {
        validateUser(user);
        log.debug("{}", chatLogs);


        List<ChatLog> messages = chatLogs.get(user);

        // Check if the message with the given msgId exists
        boolean messageExists = messages.stream()
                .anyMatch(chatLog -> chatLog.getId().equals(msgId));

        if (!messageExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Message with ID " + msgId + " not found for user " + user);
        }

        messages = messages.stream()
            .filter(chatLog -> !chatLog.getId().equals(msgId))
            .collect(Collectors.toCollection(ArrayList::new));

        log.debug("{}", chatLogs);

        chatLogs.put(user, messages);
    }

    private void validateUserLength(String user) {
        if (user.length() > 16) {
            throw new IllegalArgumentException("user id must be of length 16 or less");
        }
    }

    private void validateUser(String user) {
        if (!chatLogs.containsKey(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user id not present");
        }
        validateUserLength(user);
    }
}