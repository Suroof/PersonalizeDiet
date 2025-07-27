package com.itheima.zhinengti.controller;

import com.itheima.zhinengti.entity.User;
import com.itheima.zhinengti.entity.UserPreference;
import com.itheima.zhinengti.repository.UserRepository;
import com.itheima.zhinengti.repository.UserPreferenceRepository;
import com.itheima.zhinengti.dto.UserPreferenceRequest;
import com.itheima.zhinengti.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserPreferencesController {

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/interest")
    public ResponseEntity<?> updatePreferences(@RequestBody UserPreferenceRequest request) {
        try {
            // 从Session获取当前用户ID
            Long userId = SessionUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(401).body("请先登录");
            }
            
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body("用户不存在");
            }

            // 查找或创建用户偏好
            UserPreference preference = userPreferenceRepository.findByUserId(userId);
            if (preference == null) {
                preference = new UserPreference();
                preference.setUser(user);
            }

            // 更新偏好设置
            preference.setDietTypes(request.getDietTypes());
            preference.setAllergens(request.getAllergens());
            preference.setDislikedIngredients(request.getDislikedIngredients());
            preference.setSpiciness(request.getSpiciness());
            preference.setSweetness(request.getSweetness());
            preference.setSaltiness(request.getSaltiness());
            preference.setMaxCookingTime(request.getMaxCookingTime());

            userPreferenceRepository.save(preference);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "偏好设置保存成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "保存失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/interest")
    public ResponseEntity<?> getPreferences() {
        try {
            // 从Session获取当前用户ID
            Long userId = SessionUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(401).body("请先登录");
            }
            
            UserPreference preference = userPreferenceRepository.findByUserId(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取成功");
            response.put("data", preference);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}


