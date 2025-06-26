package com.itheima.zhinengti.controller;

import com.itheima.zhinengti.dto.ApiResponse;
import com.itheima.zhinengti.dto.ChatRequest;
import com.itheima.zhinengti.dto.ChatResponse;
import com.itheima.zhinengti.service.DifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {
    
    @Autowired
    private DifyService difyService;
    
    /**
     * 发送聊天消息
     */
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<ChatResponse>> sendMessage(@RequestBody ChatRequest request) {
        try {
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "消息内容不能为空", null));
            }
            
            ChatResponse response = difyService.sendChatMessage(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "发送成功", response));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(false, "发送失败: " + e.getMessage(), null));
        }
    }
    
    /**
     * AI聊天接口
     */
    @PostMapping("/ai-chat")
    public ResponseEntity<ApiResponse<ChatResponse>> aiChat(@RequestBody ChatRequest request) {
        try {
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "消息内容不能为空", null));
            }
            
            ChatResponse response = difyService.sendChatMessage(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "AI聊天成功", response));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(false, "AI聊天失败: " + e.getMessage(), null));
        }
    }
    
    /**
     * 流式聊天接口（暂时返回普通响应）
     */
    @PostMapping("/stream")
    public ResponseEntity<ApiResponse<ChatResponse>> streamChat(@RequestBody ChatRequest request) {
        try {
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "消息内容不能为空", null));
            }
            
            // 暂时使用普通聊天接口，后续可以实现真正的流式响应
            ChatResponse response = difyService.sendChatMessage(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "流式聊天成功", response));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(false, "流式聊天失败: " + e.getMessage(), null));
        }
    }
}