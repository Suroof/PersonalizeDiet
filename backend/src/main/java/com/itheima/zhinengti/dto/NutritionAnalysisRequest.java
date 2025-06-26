package com.itheima.zhinengti.dto;

import lombok.Data;

@Data
public class NutritionAnalysisRequest {
    private String text;
    private String user;
    
    // 构造函数
    public NutritionAnalysisRequest() {}
    
    public NutritionAnalysisRequest(String text) {
        this.text = text;
        this.user = "user-" + System.currentTimeMillis();
    }
}