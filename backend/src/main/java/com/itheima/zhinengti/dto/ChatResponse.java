package com.itheima.zhinengti.dto;

import lombok.Data;

@Data
public class ChatResponse {
    private String answer;
    private String conversationId;
    private String messageId;
    private String mode;
    private Object metadata;
    
    // 构造函数
    public ChatResponse() {}
    
    public ChatResponse(String answer) {
        this.answer = answer;
    }
}