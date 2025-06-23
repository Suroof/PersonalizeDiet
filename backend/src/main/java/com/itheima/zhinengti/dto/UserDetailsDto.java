package com.itheima.zhinengti.dto;

import com.itheima.zhinengti.entity.User;

public class UserDetailsDto {
    private Long id;
    private String username;
    private String email;

    // Constructors
    public UserDetailsDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
}