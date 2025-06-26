package com.itheima.zhinengti.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NutritionAnalysisResponse {
    private String analysis;
    private String inputText;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private LocalDateTime timestamp;
    
    // 构造函数
    public NutritionAnalysisResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public NutritionAnalysisResponse(String analysis, String inputText) {
        this.analysis = analysis;
        this.inputText = inputText;
        this.timestamp = LocalDateTime.now();
    }
}