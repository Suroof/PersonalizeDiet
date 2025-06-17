package com.personalize.diet.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 用户收藏实体类
 * 
 * @author PersonalizeDiet Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_favorites")
public class UserFavorite extends BaseEntity {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @TableField("user_id")
    private Long userId;

    /**
     * 收藏类型（1：菜谱，2：食材，3：聊天记录，4：其他）
     */
    @NotNull(message = "收藏类型不能为空")
    @TableField("favorite_type")
    private Integer favoriteType;

    /**
     * 收藏目标ID
     */
    @NotNull(message = "收藏目标ID不能为空")
    @TableField("target_id")
    private Long targetId;

    /**
     * 收藏目标名称（冗余字段，便于查询）
     */
    @Size(max = 200, message = "收藏目标名称长度不能超过200个字符")
    @TableField("target_name")
    private String targetName;

    /**
     * 收藏目标描述
     */
    @Size(max = 500, message = "收藏目标描述长度不能超过500个字符")
    @TableField("target_description")
    private String targetDescription;

    /**
     * 收藏目标图片URL
     */
    @Size(max = 500, message = "图片URL长度不能超过500个字符")
    @TableField("target_image")
    private String targetImage;

    /**
     * 收藏分组ID
     */
    @TableField("group_id")
    private Long groupId;

    /**
     * 收藏分组名称
     */
    @Size(max = 50, message = "收藏分组名称长度不能超过50个字符")
    @TableField("group_name")
    private String groupName;

    /**
     * 收藏标签（JSON格式存储）
     * 例如：["早餐", "简单", "营养"]
     */
    @TableField("tags")
    private String tags;

    /**
     * 收藏备注
     */
    @Size(max = 500, message = "收藏备注长度不能超过500个字符")
    @TableField("note")
    private String note;

    /**
     * 收藏优先级（1：低，2：中，3：高）
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 是否公开（0：私有，1：公开）
     */
    @TableField("is_public")
    private Boolean isPublic;

    /**
     * 排序权重
     */
    @TableField("sort_weight")
    private Integer sortWeight;

    /**
     * 访问次数
     */
    @TableField("access_count")
    private Integer accessCount;

    /**
     * 最后访问时间
     */
    @TableField("last_access_time")
    private java.time.LocalDateTime lastAccessTime;

    /**
     * 扩展数据（JSON格式存储）
     */
    @TableField("extra_data")
    private String extraData;

    // 常量定义

    /**
     * 收藏类型常量
     */
    public static final int TYPE_RECIPE = 1;
    public static final int TYPE_INGREDIENT = 2;
    public static final int TYPE_CHAT = 3;
    public static final int TYPE_OTHER = 4;

    /**
     * 优先级常量
     */
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_HIGH = 3;
}