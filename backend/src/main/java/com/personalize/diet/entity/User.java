package com.personalize.diet.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 
 * @author PersonalizeDiet Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("users")
public class User extends BaseEntity {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    @TableField("username")
    private String username;

    /**
     * 密码（加密后）
     */
    @JsonIgnore
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6-100个字符之间")
    @TableField("password")
    private String password;

    /**
     * 昵称
     */
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    @TableField("nickname")
    private String nickname;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    @TableField("email")
    private String email;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @TableField("phone")
    private String phone;

    /**
     * 头像URL
     */
    @Size(max = 500, message = "头像URL长度不能超过500个字符")
    @TableField("avatar")
    private String avatar;

    /**
     * 性别（0：未知，1：男，2：女）
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 生日
     */
    @TableField("birthday")
    private LocalDate birthday;

    /**
     * 身高（厘米）
     */
    @TableField("height")
    private Integer height;

    /**
     * 体重（千克）
     */
    @TableField("weight")
    private Double weight;

    /**
     * 活动水平（1：久坐，2：轻度活动，3：中度活动，4：重度活动，5：极重度活动）
     */
    @TableField("activity_level")
    private Integer activityLevel;

    /**
     * 健康目标（1：减重，2：增重，3：维持，4：增肌，5：塑形）
     */
    @TableField("health_goal")
    private Integer healthGoal;

    /**
     * 饮食偏好（JSON格式存储）
     * 例如：{"vegetarian": true, "spicy": false, "sweet": true}
     */
    @TableField("dietary_preferences")
    private String dietaryPreferences;

    /**
     * 过敏信息（JSON格式存储）
     * 例如：["花生", "海鲜", "牛奶"]
     */
    @TableField("allergies")
    private String allergies;

    /**
     * 不喜欢的食材（JSON格式存储）
     * 例如：["香菜", "胡萝卜", "青椒"]
     */
    @TableField("dislikes")
    private String dislikes;

    /**
     * 烹饪技能等级（1：新手，2：初级，3：中级，4：高级，5：专家）
     */
    @TableField("cooking_skill")
    private Integer cookingSkill;

    /**
     * 可用烹饪时间（分钟）
     */
    @TableField("available_time")
    private Integer availableTime;

    /**
     * 厨房设备（JSON格式存储）
     * 例如：["微波炉", "烤箱", "空气炸锅", "电饭煲"]
     */
    @TableField("kitchen_equipment")
    private String kitchenEquipment;

    /**
     * 预算范围（1：经济型，2：中等，3：高端）
     */
    @TableField("budget_range")
    private Integer budgetRange;

    /**
     * 用户状态（0：禁用，1：正常，2：锁定）
     */
    @TableField("status")
    private Integer status;

    /**
     * 最后登录时间
     */
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    @TableField("last_login_ip")
    private String lastLoginIp;

    /**
     * 登录次数
     */
    @TableField("login_count")
    private Integer loginCount;

    /**
     * 是否首次登录
     */
    @TableField("is_first_login")
    private Boolean isFirstLogin;

    /**
     * 用户来源（1：注册，2：微信，3：QQ，4：微博）
     */
    @TableField("source")
    private Integer source;

    /**
     * 第三方用户ID
     */
    @TableField("third_party_id")
    private String thirdPartyId;

    /**
     * 用户标签（JSON格式存储）
     * 例如：["素食主义者", "健身爱好者", "烘焙达人"]
     */
    @TableField("tags")
    private String tags;

    /**
     * 用户积分
     */
    @TableField("points")
    private Integer points;

    /**
     * 用户等级
     */
    @TableField("level")
    private Integer level;

    /**
     * VIP到期时间
     */
    @TableField("vip_expire_time")
    private LocalDateTime vipExpireTime;

    /**
     * 个人简介
     */
    @Size(max = 500, message = "个人简介长度不能超过500个字符")
    @TableField("bio")
    private String bio;

    /**
     * 地区
     */
    @Size(max = 100, message = "地区长度不能超过100个字符")
    @TableField("region")
    private String region;

    /**
     * 时区
     */
    @TableField("timezone")
    private String timezone;

    /**
     * 语言偏好
     */
    @TableField("language")
    private String language;

    /**
     * 通知设置（JSON格式存储）
     * 例如：{"email": true, "sms": false, "push": true}
     */
    @TableField("notification_settings")
    private String notificationSettings;

    /**
     * 隐私设置（JSON格式存储）
     * 例如：{"profile_public": true, "recipe_public": false}
     */
    @TableField("privacy_settings")
    private String privacySettings;

    // 常量定义
    
    /**
     * 性别常量
     */
    public static final int GENDER_UNKNOWN = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    /**
     * 活动水平常量
     */
    public static final int ACTIVITY_SEDENTARY = 1;
    public static final int ACTIVITY_LIGHT = 2;
    public static final int ACTIVITY_MODERATE = 3;
    public static final int ACTIVITY_HEAVY = 4;
    public static final int ACTIVITY_EXTREME = 5;

    /**
     * 健康目标常量
     */
    public static final int GOAL_LOSE_WEIGHT = 1;
    public static final int GOAL_GAIN_WEIGHT = 2;
    public static final int GOAL_MAINTAIN = 3;
    public static final int GOAL_BUILD_MUSCLE = 4;
    public static final int GOAL_BODY_SHAPING = 5;

    /**
     * 烹饪技能常量
     */
    public static final int SKILL_BEGINNER = 1;
    public static final int SKILL_BASIC = 2;
    public static final int SKILL_INTERMEDIATE = 3;
    public static final int SKILL_ADVANCED = 4;
    public static final int SKILL_EXPERT = 5;

    /**
     * 预算范围常量
     */
    public static final int BUDGET_ECONOMY = 1;
    public static final int BUDGET_MEDIUM = 2;
    public static final int BUDGET_HIGH = 3;

    /**
     * 用户状态常量
     */
    public static final int STATUS_DISABLED = 0;
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_LOCKED = 2;

    /**
     * 用户来源常量
     */
    public static final int SOURCE_REGISTER = 1;
    public static final int SOURCE_WECHAT = 2;
    public static final int SOURCE_QQ = 3;
    public static final int SOURCE_WEIBO = 4;
}