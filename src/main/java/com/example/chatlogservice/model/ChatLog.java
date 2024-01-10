package com.example.chatlogservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatLog {
    @JsonIgnore
    private String id;
    private String message;
    private long timestamp;
    private boolean isSent;
}