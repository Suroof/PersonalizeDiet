package com.personalize.diet.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 聊天会话实体类
 * 
 * @author PersonalizeDiet Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("chat_sessions")
public class ChatSession extends BaseEntity {

    /**
     * 会话标题
     */
    @Size(max = 100, message = "会话标题长度不能超过100个字符")
    @TableField("title")
    private String title;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * Dify会话ID
     */
    @NotBlank(message = "Dify会话ID不能为空")
    @Size(max = 100, message = "Dify会话ID长度不能超过100个字符")
    @TableField("dify_session_id")
    private String difySessionId;

    /**
     * Dify对话ID
     */
    @Size(max = 100, message = "Dify对话ID长度不能超过100个字符")
    @TableField("dify_conversation_id")
    private String difyConversationId;

    /**
     * 会话类型（1：菜谱推荐，2：营养咨询，3：烹饪指导，4：其他）
     */
    @TableField("session_type")
    private Integer sessionType;

    /**
     * 会话状态（1：活跃，2：已结束，3：已暂停）
     */
    @TableField("status")
    private Integer status;

    /**
     * 消息总数
     */
    @TableField("message_count")
    private Integer messageCount;

    /**
     * 最后一条消息内容
     */
    @Size(max = 500, message = "最后消息内容长度不能超过500个字符")
    @TableField("last_message")
    private String lastMessage;

    /**
     * 最后消息时间
     */
    @TableField("last_message_time")
    private LocalDateTime lastMessageTime;

    /**
     * 最后消息发送者类型（1：用户，2：AI助手）
     */
    @TableField("last_message_sender")
    private Integer lastMessageSender;

    /**
     * 会话上下文信息（JSON格式存储）
     * 例如：{"user_preferences": {...}, "current_topic": "recipe_recommendation", "context_data": {...}}
     */
    @TableField("context_data")
    private String contextData;

    /**
     * 用户偏好设置（JSON格式存储）
     * 例如：{"dietary_restrictions": [...], "favorite_cuisines": [...], "cooking_skill": 2}
     */
    @TableField("user_preferences")
    private String userPreferences;

    /**
     * 会话标签（JSON格式存储）
     * 例如：["减肥餐", "素食", "快手菜"]
     */
    @TableField("tags")
    private String tags;

    /**
     * 推荐的菜谱ID列表（JSON格式存储）
     * 例如：[1001, 1002, 1003]
     */
    @TableField("recommended_recipes")
    private String recommendedRecipes;

    /**
     * 会话评分（1-5分）
     */
    @TableField("rating")
    private Integer rating;

    /**
     * 会话反馈
     */
    @Size(max = 500, message = "会话反馈长度不能超过500个字符")
    @TableField("feedback")
    private String feedback;

    /**
     * 是否置顶
     */
    @TableField("is_pinned")
    private Boolean isPinned;

    /**
     * 是否收藏
     */
    @TableField("is_favorite")
    private Boolean isFavorite;

    /**
     * 会话过期时间
     */
    @TableField("expire_time")
    private LocalDateTime expireTime;

    /**
     * 客户端IP地址
     */
    @Size(max = 45, message = "IP地址长度不能超过45个字符")
    @TableField("client_ip")
    private String clientIp;

    /**
     * 用户代理信息
     */
    @Size(max = 500, message = "用户代理信息长度不能超过500个字符")
    @TableField("user_agent")
    private String userAgent;

    /**
     * 设备类型（1：PC，2：移动端，3：平板）
     */
    @TableField("device_type")
    private Integer deviceType;

    /**
     * 会话来源（1：网页，2：APP，3：小程序，4：API）
     */
    @TableField("source")
    private Integer source;

    /**
     * 会话统计信息（JSON格式存储）
     * 例如：{"total_tokens": 1500, "user_messages": 8, "ai_messages": 7, "avg_response_time": 2.5}
     */
    @TableField("statistics")
    private String statistics;

    /**
     * 错误信息（JSON格式存储）
     * 例如：[{"timestamp": "2024-01-01 10:00:00", "error_code": "DIFY_API_ERROR", "message": "API调用失败"}]
     */
    @TableField("error_logs")
    private String errorLogs;

    // 常量定义

    /**
     * 会话类型常量
     */
    public static final int TYPE_RECIPE_RECOMMENDATION = 1;
    public static final int TYPE_NUTRITION_CONSULTATION = 2;
    public static final int TYPE_COOKING_GUIDANCE = 3;
    public static final int TYPE_OTHER = 4;

    /**
     * 会话状态常量
     */
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_ENDED = 2;
    public static final int STATUS_PAUSED = 3;

    /**
     * 消息发送者类型常量
     */
    public static final int SENDER_USER = 1;
    public static final int SENDER_AI = 2;

    /**
     * 设备类型常量
     */
    public static final int DEVICE_PC = 1;
    public static final int DEVICE_MOBILE = 2;
    public static final int DEVICE_TABLET = 3;

    /**
     * 会话来源常量
     */
    public static final int SOURCE_WEB = 1;
    public static final int SOURCE_APP = 2;
    public static final int SOURCE_MINIPROGRAM = 3;
    public static final int SOURCE_API = 4;
}