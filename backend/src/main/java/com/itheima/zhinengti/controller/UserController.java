package com.itheima.zhinengti.controller;

import com.itheima.zhinengti.dto.UserRequest;
import com.itheima.zhinengti.entity.User;
import com.itheima.zhinengti.repository.UserRepository;
import com.itheima.zhinengti.service.AuthService;
import com.itheima.zhinengti.util.SessionUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        // 检查用户名或邮箱是否已存在
        if (userRepository.findByUsername(userRequest.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (userRepository.findByEmail(userRequest.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        // 创建新用户
        User newUser = new User();
        newUser.setUsername(userRequest.getUsername());
        newUser.setEmail(userRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        newUser.setCreatedAt(new Date());

        userRepository.save(newUser);

        return ResponseEntity.ok("User created successfully");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
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

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", user);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("获取用户信息失败");
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UserRequest userRequest) {
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

            // 更新用户信息
            if (userRequest.getUsername() != null) {
                user.setUsername(userRequest.getUsername());
            }
            if (userRequest.getEmail() != null) {
                user.setEmail(userRequest.getEmail());
            }
            
            userRepository.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "用户信息更新成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("更新失败");
        }
    }

}
