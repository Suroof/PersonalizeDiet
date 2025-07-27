package com.itheima.zhinengti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 禁用CSRF保护
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 启用 CORS 并使用自定义配置
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/users").permitAll()
                        .requestMatchers("/api/users/profile").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/chat/**").permitAll()
                        .requestMatchers("/api/nutrition/**").permitAll()
                        .requestMatchers("/api/recipes/**").permitAll()
                        .requestMatchers("/api/interest/**").permitAll()
                        .requestMatchers("/api/user-preferences/**").permitAll()
                        .anyRequest().authenticated()
                )
                   .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 允许创建Session
                );

        return http.build();
    }

    // 配置 CORS 允许的来源、方法、头部等
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 前端地址
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // 是否允许发送 Cookie

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
