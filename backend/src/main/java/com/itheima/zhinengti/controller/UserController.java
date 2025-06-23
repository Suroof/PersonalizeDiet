package com.itheima.zhinengti.controller;

import com.itheima.zhinengti.dto.UserRequest;
import com.itheima.zhinengti.entity.User;
import com.itheima.zhinengti.repository.UserRepository;
import com.itheima.zhinengti.service.AuthService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Date;

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
}
