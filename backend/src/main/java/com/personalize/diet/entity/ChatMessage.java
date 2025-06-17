package com.personalize.diet.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 聊天消息实体类
 * 
 * @author PersonalizeDiet Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("chat_messages")
public class ChatMessage extends BaseEntity {

    /**
     * 会话ID
     */
    @NotNull(message = "会话ID不能为空")
    @TableField("session_id")
    private Long sessionId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * Dify消息ID
     */
    @Size(max = 100, message = "Dify消息ID长度不能超过100个字符")
    @TableField("dify_message_id")
    private String difyMessageId;

    /**
     * 父消息ID（用于回复消息）
     */
    @TableField("parent_message_id")
    private Long parentMessageId;

    /**
     * 消息类型（1：文本，2：图片，3：语音，4：视频，5：文件，6：菜谱卡片，7：系统消息）
     */
    @NotNull(message = "消息类型不能为空")
    @TableField("message_type")
    private Integer messageType;

    /**
     * 发送者类型（1：用户，2：AI助手，3：系统）
     */
    @NotNull(message = "发送者类型不能为空")
    @TableField("sender_type")
    private Integer senderType;

    /**
     * 发送者ID
     */
    @TableField("sender_id")
    private Long senderId;

    /**
     * 发送者名称
     */
    @Size(max = 50, message = "发送者名称长度不能超过50个字符")
    @TableField("sender_name")
    private String senderName;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    @TableField("content")
    private String content;

    /**
     * 原始内容（未处理的内容）
     */
    @TableField("raw_content")
    private String rawContent;

    /**
     * 格式化内容（Markdown等格式化后的内容）
     */
    @TableField("formatted_content")
    private String formattedContent;

    /**
     * 消息附件信息（JSON格式存储）
     * 例如：{"type": "image", "url": "http://...", "size": 1024, "name": "image.jpg"}
     */
    @TableField("attachments")
    private String attachments;

    /**
     * 消息元数据（JSON格式存储）
     * 例如：{"tokens": 150, "model": "gpt-3.5-turbo", "temperature": 0.7, "response_time": 2.5}
     */
    @TableField("metadata")
    private String metadata;

    /**
     * 消息状态（1：发送中，2：已发送，3：已读，4：发送失败，5：已撤回）
     */
    @TableField("status")
    private Integer status;

    /**
     * 错误信息
     */
    @Size(max = 500, message = "错误信息长度不能超过500个字符")
    @TableField("error_message")
    private String errorMessage;

    /**
     * 重试次数
     */
    @TableField("retry_count")
    private Integer retryCount;

    /**
     * 是否流式消息
     */
    @TableField("is_streaming")
    private Boolean isStreaming;

    /**
     * 流式消息序号
     */
    @TableField("stream_sequence")
    private Integer streamSequence;

    /**
     * 是否流式消息结束
     */
    @TableField("is_stream_end")
    private Boolean isStreamEnd;

    /**
     * 消息序号（在会话中的顺序）
     */
    @TableField("sequence_number")
    private Integer sequenceNumber;

    /**
     * 消息标签（JSON格式存储）
     * 例如：["重要", "菜谱推荐", "营养建议"]
     */
    @TableField("tags")
    private String tags;

    /**
     * 相关菜谱ID列表（JSON格式存储）
     * 例如：[1001, 1002, 1003]
     */
    @TableField("related_recipes")
    private String relatedRecipes;

    /**
     * 意图识别结果（JSON格式存储）
     * 例如：{"intent": "recipe_search", "entities": [{"type": "ingredient", "value": "鸡蛋"}], "confidence": 0.95}
     */
    @TableField("intent_data")
    private String intentData;

    /**
     * 情感分析结果（JSON格式存储）
     * 例如：{"sentiment": "positive", "confidence": 0.8, "emotions": ["joy", "satisfaction"]}
     */
    @TableField("sentiment_data")
    private String sentimentData;

    /**
     * 消息评分（1-5分）
     */
    @TableField("rating")
    private Integer rating;

    /**
     * 消息反馈
     */
    @Size(max = 500, message = "消息反馈长度不能超过500个字符")
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
     * 阅读时间
     */
    @TableField("read_time")
    private LocalDateTime readTime;

    /**
     * 撤回时间
     */
    @TableField("recall_time")
    private LocalDateTime recallTime;

    /**
     * 编辑时间
     */
    @TableField("edit_time")
    private LocalDateTime editTime;

    /**
     * 编辑次数
     */
    @TableField("edit_count")
    private Integer editCount;

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
     * 消息来源（1：网页，2：APP，3：小程序，4：API）
     */
    @TableField("source")
    private Integer source;

    /**
     * 处理时间（毫秒）
     */
    @TableField("processing_time")
    private Long processingTime;

    /**
     * Token使用量
     */
    @TableField("token_usage")
    private Integer tokenUsage;

    /**
     * 成本（分）
     */
    @TableField("cost")
    private Integer cost;

    /**
     * 扩展数据（JSON格式存储）
     */
    @TableField("extra_data")
    private String extraData;

    // 常量定义

    /**
     * 消息类型常量
     */
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_IMAGE = 2;
    public static final int TYPE_VOICE = 3;
    public static final int TYPE_VIDEO = 4;
    public static final int TYPE_FILE = 5;
    public static final int TYPE_RECIPE_CARD = 6;
    public static final int TYPE_SYSTEM = 7;

    /**
     * 发送者类型常量
     */
    public static final int SENDER_USER = 1;
    public static final int SENDER_AI = 2;
    public static final int SENDER_SYSTEM = 3;

    /**
     * 消息状态常量
     */
    public static final int STATUS_SENDING = 1;
    public static final int STATUS_SENT = 2;
    public static final int STATUS_READ = 3;
    public static final int STATUS_FAILED = 4;
    public static final int STATUS_RECALLED = 5;

    /**
     * 设备类型常量
     */
    public static final int DEVICE_PC = 1;
    public static final int DEVICE_MOBILE = 2;
    public static final int DEVICE_TABLET = 3;

    /**
     * 消息来源常量
     */
    public static final int SOURCE_WEB = 1;
    public static final int SOURCE_APP = 2;
    public static final int SOURCE_MINIPROGRAM = 3;
    public static final int SOURCE_API = 4;
}