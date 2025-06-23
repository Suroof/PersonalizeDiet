package com.itheima.zhinengti.service;

import com.itheima.zhinengti.dto.LoginRequest;
import com.itheima.zhinengti.entity.User;
import com.itheima.zhinengti.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User authenticate(LoginRequest request) {
        // 尝试通过用户名或邮箱查找用户
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            user = userRepository.findByEmail(request.getUsername());
        }

        // 验证用户存在且密码匹配
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return null;
        }
        return user;
    }
}