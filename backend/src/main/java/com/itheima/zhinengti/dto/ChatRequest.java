package com.itheima.zhinengti.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private String message;
    private String conversationId;
    private String user;
    
    // 构造函数
    public ChatRequest() {}
    
    public ChatRequest(String message) {
        this.message = message;
        this.user = "user-" + System.currentTimeMillis();
    }
}