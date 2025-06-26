package com.itheima.zhinengti.service;

import com.itheima.zhinengti.dto.ChatRequest;
import com.itheima.zhinengti.dto.ChatResponse;
import com.itheima.zhinengti.dto.NutritionAnalysisRequest;
import com.itheima.zhinengti.dto.NutritionAnalysisResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class DifyService {
    
    @Value("${dify.api.url:https://api.dify.ai/v1}")
    private String difyApiUrl;
    
    @Value("${dify.api.key:}")
    private String difyApiKey;
    
    @Value("${dify.nutrition.api.url:https://api.dify.ai/v1}")
    private String nutritionDifyApiUrl;
    
    @Value("${dify.nutrition.api.key:}")
    private String nutritionDifyApiKey;
    
    private final RestTemplate restTemplate;
    
    public DifyService() {
        this.restTemplate = new RestTemplate();
    }
    
    /**
     * 发送聊天消息到Dify
     */
    public ChatResponse sendChatMessage(ChatRequest request) {
        if (difyApiKey == null || difyApiKey.isEmpty()) {
            throw new RuntimeException("Dify API Key 未配置");
        }
        
        String url = difyApiUrl + "/chat-messages";
        
        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("inputs", new HashMap<>());
        requestBody.put("query", request.getMessage());
        requestBody.put("response_mode", "blocking");
        requestBody.put("conversation_id", request.getConversationId() != null ? request.getConversationId() : "");
        requestBody.put("user", request.getUser() != null ? request.getUser() : "user-" + System.currentTimeMillis());
        
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(difyApiKey);
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                ChatResponse chatResponse = new ChatResponse();
                chatResponse.setAnswer((String) responseBody.get("answer"));
                chatResponse.setConversationId((String) responseBody.get("conversation_id"));
                chatResponse.setMessageId((String) responseBody.get("id"));
                chatResponse.setMode((String) responseBody.get("mode"));
                chatResponse.setMetadata(responseBody.get("metadata"));
                
                return chatResponse;
            } else {
                throw new RuntimeException("Dify API 响应错误: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("调用 Dify API 失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 发送营养分析请求到Dify
     */
    public NutritionAnalysisResponse analyzeNutrition(NutritionAnalysisRequest request) {
        if (nutritionDifyApiKey == null || nutritionDifyApiKey.isEmpty()) {
            throw new RuntimeException("营养分析 API Key 未配置");
        }
        
        if (request.getText() == null || request.getText().trim().isEmpty()) {
            throw new RuntimeException("请输入要分析的食物信息");
        }
        
        if (request.getText().length() > 2000) {
            throw new RuntimeException("文本长度不能超过2000字符");
        }
        
        String url = nutritionDifyApiUrl + "/chat-messages";
        
        // 构建查询内容
        String query = "请分析以下食物信息的营养成分：" + request.getText() + 
                      "\n\n请提供详细的营养成分分析，包括热量、蛋白质、脂肪、碳水化合物、维生素和矿物质含量。";
        
        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("inputs", new HashMap<>());
        requestBody.put("query", query);
        requestBody.put("response_mode", "blocking");
        requestBody.put("conversation_id", "");
        requestBody.put("user", request.getUser() != null ? request.getUser() : "user-" + System.currentTimeMillis());
        
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(nutritionDifyApiKey);
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String analysis = (String) responseBody.get("answer");
                
                if (analysis != null && !analysis.isEmpty()) {
                    return new NutritionAnalysisResponse(analysis, request.getText());
                } else {
                    throw new RuntimeException("营养分析响应格式错误");
                }
            } else {
                throw new RuntimeException("营养分析 API 响应错误: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("调用营养分析 API 失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 上传文件进行营养分析
     */
    public NutritionAnalysisResponse analyzeNutritionFile(MultipartFile file) {
        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/") && 
            !contentType.equals("text/plain") && 
            !contentType.equals("application/pdf"))) {
            throw new RuntimeException("不支持的文件类型，请上传图片、文本或PDF文件");
        }
        
        // 检查文件大小 (10MB)
        long maxSize = 10 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new RuntimeException("文件大小不能超过10MB");
        }
        
        // TODO: 实现文件上传到Dify的逻辑
        // 这里需要先上传文件到Dify，然后获取文件ID，再进行分析
        // 由于涉及到文件上传的复杂逻辑，暂时抛出异常提示
        throw new RuntimeException("文件上传功能暂未实现，请使用文本分析功能");
    }
}