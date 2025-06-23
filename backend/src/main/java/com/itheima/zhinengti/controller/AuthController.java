package com.itheima.zhinengti.controller;


import com.itheima.zhinengti.dto.ApiResponse;
import com.itheima.zhinengti.dto.LoginRequest;
import com.itheima.zhinengti.dto.UserDetailsDto;
import com.itheima.zhinengti.entity.User;
import com.itheima.zhinengti.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = authService.authenticate(loginRequest);
            if (user == null) {
                return new ApiResponse<>(false, "用户名或密码错误");
            }
            return new ApiResponse<>(true, new UserDetailsDto(user));
        } catch (Exception e) {
            return new ApiResponse<>(false, "登录请求处理失败，请重试");
        }
    }
}