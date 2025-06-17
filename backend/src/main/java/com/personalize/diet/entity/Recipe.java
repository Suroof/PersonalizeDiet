package com.personalize.diet.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 菜谱实体类
 * 
 * @author PersonalizeDiet Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("recipes")
public class Recipe extends BaseEntity {

    /**
     * 菜谱名称
     */
    @NotBlank(message = "菜谱名称不能为空")
    @Size(max = 100, message = "菜谱名称长度不能超过100个字符")
    @TableField("name")
    private String name;

    /**
     * 菜谱描述
     */
    @Size(max = 500, message = "菜谱描述长度不能超过500个字符")
    @TableField("description")
    private String description;

    /**
     * 菜谱封面图片URL
     */
    @Size(max = 500, message = "图片URL长度不能超过500个字符")
    @TableField("cover_image")
    private String coverImage;

    /**
     * 菜谱图片集合（JSON格式存储）
     * 例如：["url1", "url2", "url3"]
     */
    @TableField("images")
    private String images;

    /**
     * 菜谱分类ID
     */
    @NotNull(message = "菜谱分类不能为空")
    @TableField("category_id")
    private Long categoryId;

    /**
     * 菜谱分类名称（冗余字段，便于查询）
     */
    @TableField("category_name")
    private String categoryName;

    /**
     * 菜系类型（1：川菜，2：粤菜，3：鲁菜，4：苏菜，5：浙菜，6：闽菜，7：湘菜，8：徽菜，9：其他）
     */
    @TableField("cuisine_type")
    private Integer cuisineType;

    /**
     * 难度等级（1：简单，2：普通，3：困难）
     */
    @NotNull(message = "难度等级不能为空")
    @Min(value = 1, message = "难度等级最小为1")
    @Max(value = 3, message = "难度等级最大为3")
    @TableField("difficulty")
    private Integer difficulty;

    /**
     * 准备时间（分钟）
     */
    @Min(value = 0, message = "准备时间不能为负数")
    @TableField("prep_time")
    private Integer prepTime;

    /**
     * 烹饪时间（分钟）
     */
    @NotNull(message = "烹饪时间不能为空")
    @Min(value = 1, message = "烹饪时间最少为1分钟")
    @TableField("cook_time")
    private Integer cookTime;

    /**
     * 总时间（分钟）
     */
    @TableField("total_time")
    private Integer totalTime;

    /**
     * 份数
     */
    @NotNull(message = "份数不能为空")
    @Min(value = 1, message = "份数最少为1")
    @Max(value = 20, message = "份数最多为20")
    @TableField("servings")
    private Integer servings;

    /**
     * 食材清单（JSON格式存储）
     * 例如：[{"name":"鸡蛋","amount":"2个","category":"蛋类"},{"name":"面粉","amount":"100g","category":"主料"}]
     */
    @NotBlank(message = "食材清单不能为空")
    @TableField("ingredients")
    private String ingredients;

    /**
     * 制作步骤（JSON格式存储）
     * 例如：[{"step":1,"description":"将鸡蛋打散","image":"url","time":2},{"step":2,"description":"加入面粉搅拌","image":"url","time":3}]
     */
    @NotBlank(message = "制作步骤不能为空")
    @TableField("instructions")
    private String instructions;

    /**
     * 营养信息（JSON格式存储）
     * 例如：{"calories":250,"protein":12.5,"carbs":30.2,"fat":8.1,"fiber":2.3,"sugar":5.6,"sodium":450}
     */
    @TableField("nutrition")
    private String nutrition;

    /**
     * 每份热量（卡路里）
     */
    @DecimalMin(value = "0.0", message = "热量不能为负数")
    @TableField("calories_per_serving")
    private BigDecimal caloriesPerServing;

    /**
     * 烹饪技巧和小贴士（JSON格式存储）
     * 例如：["火候要掌握好","调料要适量","注意食材新鲜度"]
     */
    @TableField("tips")
    private String tips;

    /**
     * 所需厨具（JSON格式存储）
     * 例如：["平底锅","搅拌器","量杯"]
     */
    @TableField("equipment")
    private String equipment;

    /**
     * 适合人群标签（JSON格式存储）
     * 例如：["儿童","老人","孕妇","素食者"]
     */
    @TableField("suitable_for")
    private String suitableFor;

    /**
     * 菜谱标签（JSON格式存储）
     * 例如：["家常菜","下饭菜","快手菜","减脂餐"]
     */
    @TableField("tags")
    private String tags;

    /**
     * 季节适宜性（JSON格式存储）
     * 例如：["春","夏","秋","冬"]
     */
    @TableField("seasons")
    private String seasons;

    /**
     * 餐次类型（JSON格式存储）
     * 例如：["早餐","午餐","晚餐","夜宵"]
     */
    @TableField("meal_types")
    private String mealTypes;

    /**
     * 菜谱来源
     */
    @Size(max = 100, message = "菜谱来源长度不能超过100个字符")
    @TableField("source")
    private String source;

    /**
     * 原始链接
     */
    @Size(max = 500, message = "原始链接长度不能超过500个字符")
    @TableField("original_url")
    private String originalUrl;

    /**
     * 作者ID
     */
    @TableField("author_id")
    private Long authorId;

    /**
     * 作者名称
     */
    @Size(max = 50, message = "作者名称长度不能超过50个字符")
    @TableField("author_name")
    private String authorName;

    /**
     * 菜谱状态（0：草稿，1：已发布，2：已下架，3：审核中，4：审核失败）
     */
    @TableField("status")
    private Integer status;

    /**
     * 是否推荐（0：否，1：是）
     */
    @TableField("is_recommended")
    private Boolean isRecommended;

    /**
     * 是否热门（0：否，1：是）
     */
    @TableField("is_hot")
    private Boolean isHot;

    /**
     * 是否精选（0：否，1：是）
     */
    @TableField("is_featured")
    private Boolean isFeatured;

    /**
     * 浏览次数
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 收藏次数
     */
    @TableField("favorite_count")
    private Integer favoriteCount;

    /**
     * 点赞次数
     */
    @TableField("like_count")
    private Integer likeCount;

    /**
     * 评论次数
     */
    @TableField("comment_count")
    private Integer commentCount;

    /**
     * 制作次数
     */
    @TableField("made_count")
    private Integer madeCount;

    /**
     * 分享次数
     */
    @TableField("share_count")
    private Integer shareCount;

    /**
     * 平均评分
     */
    @DecimalMin(value = "0.0", message = "评分不能为负数")
    @DecimalMax(value = "5.0", message = "评分不能超过5.0")
    @TableField("average_rating")
    private BigDecimal averageRating;

    /**
     * 评分次数
     */
    @TableField("rating_count")
    private Integer ratingCount;

    /**
     * 预计成本（元）
     */
    @DecimalMin(value = "0.0", message = "成本不能为负数")
    @TableField("estimated_cost")
    private BigDecimal estimatedCost;

    /**
     * 排序权重
     */
    @TableField("sort_weight")
    private Integer sortWeight;

    /**
     * SEO关键词
     */
    @Size(max = 200, message = "SEO关键词长度不能超过200个字符")
    @TableField("seo_keywords")
    private String seoKeywords;

    /**
     * SEO描述
     */
    @Size(max = 300, message = "SEO描述长度不能超过300个字符")
    @TableField("seo_description")
    private String seoDescription;

    // 常量定义

    /**
     * 菜系类型常量
     */
    public static final int CUISINE_SICHUAN = 1;
    public static final int CUISINE_CANTONESE = 2;
    public static final int CUISINE_SHANDONG = 3;
    public static final int CUISINE_JIANGSU = 4;
    public static final int CUISINE_ZHEJIANG = 5;
    public static final int CUISINE_FUJIAN = 6;
    public static final int CUISINE_HUNAN = 7;
    public static final int CUISINE_ANHUI = 8;
    public static final int CUISINE_OTHER = 9;

    /**
     * 难度等级常量
     */
    public static final int DIFFICULTY_EASY = 1;
    public static final int DIFFICULTY_MEDIUM = 2;
    public static final int DIFFICULTY_HARD = 3;

    /**
     * 菜谱状态常量
     */
    public static final int STATUS_DRAFT = 0;
    public static final int STATUS_PUBLISHED = 1;
    public static final int STATUS_OFFLINE = 2;
    public static final int STATUS_REVIEWING = 3;
    public static final int STATUS_REVIEW_FAILED = 4;
}