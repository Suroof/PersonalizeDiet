package com.itheima.zhinengti.dto;

import com.itheima.zhinengti.entity.User;

public class UserDetailsDto {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String avatar;
    private String phone;
    private String birthday;
    private String gender;

    public UserDetailsDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getUsername(); // 如果没有单独的name字段，使用username
        this.avatar = ""; // 默认值
        this.phone = "";
        this.birthday = "";
        this.gender = "";
    }

    // getters and setters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getAvatar() { return avatar; }
    public String getPhone() { return phone; }
    public String getBirthday() { return birthday; }
    public String getGender() { return gender; }
}
