package com.itheima.zhinengti.controller;

import com.itheima.zhinengti.dto.ApiResponse;
import com.itheima.zhinengti.dto.NutritionAnalysisRequest;
import com.itheima.zhinengti.dto.NutritionAnalysisResponse;
import com.itheima.zhinengti.service.DifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/nutrition")
@CrossOrigin(origins = "*")
public class NutritionController {
    
    @Autowired
    private DifyService difyService;
    
    // 简单的内存存储，实际项目中应该使用数据库
    private List<NutritionAnalysisResponse> analysisHistory = new ArrayList<>();
    
    /**
     * 文本营养分析
     */
    @PostMapping("/analyze-text")
    public ResponseEntity<ApiResponse<NutritionAnalysisResponse>> analyzeText(
            @RequestBody NutritionAnalysisRequest request) {
        try {
            if (request.getText() == null || request.getText().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "请输入要分析的食物信息", null));
            }
            
            NutritionAnalysisResponse response = difyService.analyzeNutrition(request);
            
            // 保存到历史记录
            analysisHistory.add(response);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "营养分析成功", response));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(false, "营养分析失败: " + e.getMessage(), null));
        }
    }
    
    /**
     * 文件营养分析
     */
    @PostMapping("/analyze-file")
    public ResponseEntity<ApiResponse<NutritionAnalysisResponse>> analyzeFile(
            @RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "请选择要上传的文件", null));
            }
            
            NutritionAnalysisResponse response = difyService.analyzeNutritionFile(file);
            
            // 设置文件信息
            response.setFileName(file.getOriginalFilename());
            response.setFileType(file.getContentType());
            response.setFileSize(file.getSize());
            
            // 保存到历史记录
            analysisHistory.add(response);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "文件营养分析成功", response));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(false, "文件营养分析失败: " + e.getMessage(), null));
        }
    }
    
    /**
     * 获取营养分析历史记录
     */
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<NutritionAnalysisResponse>>> getAnalysisHistory() {
        try {
            return ResponseEntity.ok(new ApiResponse<>(true, "获取历史记录成功", analysisHistory));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(false, "获取历史记录失败: " + e.getMessage(), null));
        }
    }
    
    /**
     * 清空营养分析历史记录
     */
    @DeleteMapping("/history")
    public ResponseEntity<ApiResponse<Void>> clearAnalysisHistory() {
        try {
            analysisHistory.clear();
            return ResponseEntity.ok(new ApiResponse<>(true, "历史记录已清空", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(false, "清空历史记录失败: " + e.getMessage(), null));
        }
    }
}