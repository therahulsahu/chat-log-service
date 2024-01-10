package com.example.chatlogservice.service;

import com.example.chatlogservice.model.ChatLog;

import java.util.List;

public interface ChatLogService {

    String addChatLog(String user, ChatLog message);

    List<ChatLog> getChatLogs(String user, int limit, String start);

    void deleteChatLogs(String user);

    void deleteChatLogs(String user, String msgId);

}
