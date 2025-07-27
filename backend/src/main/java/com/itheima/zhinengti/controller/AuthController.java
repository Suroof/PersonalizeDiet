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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            User user = authService.authenticate(loginRequest);
            if (user == null) {
                return new ApiResponse<>(false, "用户名或密码错误");
            }
            
            // 将用户ID保存到Session
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            
            // 构造返回数据
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("user", new UserDetailsDto(user));
            responseData.put("token", "session-based"); // 或者生成真实token
            
            System.out.println("登录成功，用户信息: " + user.getUsername() + ", ID: " + user.getId());
            
            return new ApiResponse<>(true, "登录成功", responseData);
        } catch (Exception e) {
            return new ApiResponse<>(false, "登录请求处理失败，请重试");
        }
    }
}
