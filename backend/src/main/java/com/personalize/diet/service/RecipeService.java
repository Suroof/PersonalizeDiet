package com.personalize.diet.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.diet.entity.Recipe;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜谱服务接口
 * 
 * @author PersonalizeDiet Team
 * @version 1.0.0
 */
public interface RecipeService {

    /**
     * 根据ID查询菜谱
     * 
     * @param id 菜谱ID
     * @return 菜谱信息
     */
    Recipe getById(Long id);

    /**
     * 根据菜谱名称查询菜谱
     * 
     * @param name 菜谱名称
     * @return 菜谱信息
     */
    Recipe getByName(String name);

    /**
     * 创建菜谱
     * 
     * @param recipe 菜谱信息
     * @return 创建的菜谱
     */
    Recipe createRecipe(Recipe recipe);

    /**
     * 更新菜谱信息
     * 
     * @param recipe 菜谱信息
     * @return 更新的菜谱
     */
    Recipe updateRecipe(Recipe recipe);

    /**
     * 删除菜谱
     * 
     * @param id 菜谱ID
     * @return 是否删除成功
     */
    Boolean deleteRecipe(Long id);

    /**
     * 发布菜谱
     * 
     * @param recipeId 菜谱ID
     * @param authorId 作者ID
     * @return 是否发布成功
     */
    Boolean publishRecipe(Long recipeId, Long authorId);

    /**
     * 下架菜谱
     * 
     * @param recipeId 菜谱ID
     * @param reason 下架原因
     * @return 是否下架成功
     */
    Boolean unpublishRecipe(Long recipeId, String reason);

    /**
     * 审核菜谱
     * 
     * @param recipeId 菜谱ID
     * @param status 审核状态
     * @param reviewerId 审核员ID
     * @param reviewComment 审核意见
     * @return 是否审核成功
     */
    Boolean reviewRecipe(Long recipeId, Integer status, Long reviewerId, String reviewComment);

    /**
     * 分页查询菜谱列表
     * 
     * @param page 分页参数
     * @return 菜谱分页列表
     */
    IPage<Recipe> getRecipeList(Page<Recipe> page);

    /**
     * 根据分类ID查询菜谱列表
     * 
     * @param page 分页参数
     * @param categoryId 分类ID
     * @return 菜谱分页列表
     */
    IPage<Recipe> getRecipesByCategory(Page<Recipe> page, Long categoryId);

    /**
     * 根据菜系类型查询菜谱列表
     * 
     * @param page 分页参数
     * @param cuisineType 菜系类型
     * @return 菜谱分页列表
     */
    IPage<Recipe> getRecipesByCuisine(Page<Recipe> page, Integer cuisineType);

    /**
     * 根据难度等级查询菜谱列表
     * 
     * @param page 分页参数
     * @param difficulty 难度等级
     * @return 菜谱分页列表
     */
    IPage<Recipe> getRecipesByDifficulty(Page<Recipe> page, Integer difficulty);

    /**
     * 根据烹饪时间范围查询菜谱列表
     * 
     * @param page 分页参数
     * @param minTime 最小时间
     * @param maxTime 最大时间
     * @return 菜谱分页列表
     */
    IPage<Recipe> getRecipesByCookTime(Page<Recipe> page, Integer minTime, Integer maxTime);

    /**
     * 根据热量范围查询菜谱列表
     * 
     * @param page 分页参数
     * @param minCalories 最小热量
     * @param maxCalories 最大热量
     * @return 菜谱分页列表
     */
    IPage<Recipe> getRecipesByCalories(Page<Recipe> page, BigDecimal minCalories, BigDecimal maxCalories);

    /**
     * 根据作者ID查询菜谱列表
     * 
     * @param page 分页参数
     * @param authorId 作者ID
     * @return 菜谱分页列表
     */
    IPage<Recipe> getRecipesByAuthor(Page<Recipe> page, Long authorId);

    /**
     * 查询推荐菜谱列表
     * 
     * @param page 分页参数
     * @return 菜谱分页列表
     */
    IPage<Recipe> getRecommendedRecipes(Page<Recipe> page);

    /**
     * 查询热门菜谱列表
     * 
     * @param page 分页参数
     * @return 菜谱分页列表
     */
    IPage<Recipe> getHotRecipes(Page<Recipe> page);

    /**
     * 查询精选菜谱列表
     * 
     * @param page 分页参数
     * @return 菜谱分页列表
     */
    IPage<Recipe> getFeaturedRecipes(Page<Recipe> page);

    /**
     * 根据评分范围查询菜谱列表
     * 
     * @param page 分页参数
     * @param minRating 最小评分
     * @param maxRating 最大评分
     * @return 菜谱分页列表
     */
    IPage<Recipe> getRecipesByRating(Page<Recipe> page, BigDecimal minRating, BigDecimal maxRating);

    /**
     * 根据标签查询菜谱列表
     * 
     * @param page 分页参数
     * @param tags 标签列表
     * @return 菜谱分页列表
     */
    IPage<Recipe> getRecipesByTags(Page<Recipe> page, List<String> tags);

    /**
     * 根据食材查询菜谱列表
     * 
     * @param page 分页参数
     * @param ingredients 食材列表
     * @return 菜谱分页列表
     */
    IPage<Recipe> getRecipesByIngredients(Page<Recipe> page, List<String> ingredients);

    /**
     * 搜索菜谱
     * 
     * @param page 分页参数
     * @param keyword 关键词
     * @return 菜谱分页列表
     */
    IPage<Recipe> searchRecipes(Page<Recipe> page, String keyword);

    /**
     * 个性化推荐菜谱
     * 
     * @param userId 用户ID
     * @param page 分页参数
     * @return 推荐菜谱分页列表
     */
    IPage<Recipe> getPersonalizedRecommendations(Long userId, Page<Recipe> page);

    /**
     * 根据用户偏好推荐菜谱
     * 
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 推荐菜谱列表
     */
    List<Recipe> getRecommendationsByUserPreference(Long userId, Integer limit);

    /**
     * 根据健康目标推荐菜谱
     * 
     * @param healthGoal 健康目标
     * @param limit 限制数量
     * @return 推荐菜谱列表
     */
    List<Recipe> getRecommendationsByHealthGoal(String healthGoal, Integer limit);

    /**
     * 根据饮食偏好推荐菜谱
     * 
     * @param dietaryPreferences 饮食偏好
     * @param limit 限制数量
     * @return 推荐菜谱列表
     */
    List<Recipe> getRecommendationsByDietaryPreferences(List<String> dietaryPreferences, Integer limit);

    /**
     * 增加菜谱浏览次数
     * 
     * @param recipeId 菜谱ID
     * @param userId 用户ID
     * @return 是否增加成功
     */
    Boolean incrementViewCount(Long recipeId, Long userId);

    /**
     * 收藏/取消收藏菜谱
     * 
     * @param recipeId 菜谱ID
     * @param userId 用户ID
     * @param isFavorite 是否收藏
     * @return 是否操作成功
     */
    Boolean toggleFavorite(Long recipeId, Long userId, Boolean isFavorite);

    /**
     * 点赞/取消点赞菜谱
     * 
     * @param recipeId 菜谱ID
     * @param userId 用户ID
     * @param isLike 是否点赞
     * @return 是否操作成功
     */
    Boolean toggleLike(Long recipeId, Long userId, Boolean isLike);

    /**
     * 增加菜谱制作次数
     * 
     * @param recipeId 菜谱ID
     * @param userId 用户ID
     * @return 是否增加成功
     */
    Boolean incrementMadeCount(Long recipeId, Long userId);

    /**
     * 增加菜谱分享次数
     * 
     * @param recipeId 菜谱ID
     * @param userId 用户ID
     * @return 是否增加成功
     */
    Boolean incrementShareCount(Long recipeId, Long userId);

    /**
     * 评分菜谱
     * 
     * @param recipeId 菜谱ID
     * @param userId 用户ID
     * @param rating 评分
     * @param comment 评论
     * @return 是否评分成功
     */
    Boolean rateRecipe(Long recipeId, Long userId, BigDecimal rating, String comment);

    /**
     * 更新菜谱平均评分
     * 
     * @param recipeId 菜谱ID
     * @return 是否更新成功
     */
    Boolean updateAverageRating(Long recipeId);

    /**
     * 设置菜谱推荐状态
     * 
     * @param recipeId 菜谱ID
     * @param isRecommended 是否推荐
     * @return 是否设置成功
     */
    Boolean setRecommendedStatus(Long recipeId, Boolean isRecommended);

    /**
     * 设置菜谱热门状态
     * 
     * @param recipeId 菜谱ID
     * @param isHot 是否热门
     * @return 是否设置成功
     */
    Boolean setHotStatus(Long recipeId, Boolean isHot);

    /**
     * 设置菜谱精选状态
     * 
     * @param recipeId 菜谱ID
     * @param isFeatured 是否精选
     * @return 是否设置成功
     */
    Boolean setFeaturedStatus(Long recipeId, Boolean isFeatured);

    /**
     * 更新菜谱排序权重
     * 
     * @param recipeId 菜谱ID
     * @param sortWeight 排序权重
     * @return 是否更新成功
     */
    Boolean updateSortWeight(Long recipeId, Integer sortWeight);

    /**
     * 检查菜谱名称是否存在
     * 
     * @param name 菜谱名称
     * @return 是否存在
     */
    Boolean isRecipeNameExists(String name);

    /**
     * 验证菜谱数据
     * 
     * @param recipe 菜谱信息
     * @return 验证结果
     */
    Boolean validateRecipe(Recipe recipe);

    /**
     * 解析菜谱食材
     * 
     * @param ingredientsText 食材文本
     * @return 食材列表
     */
    List<String> parseIngredients(String ingredientsText);

    /**
     * 解析菜谱步骤
     * 
     * @param stepsText 步骤文本
     * @return 步骤列表
     */
    List<String> parseSteps(String stepsText);

    /**
     * 计算菜谱营养信息
     * 
     * @param ingredients 食材列表
     * @param servings 份数
     * @return 营养信息
     */
    String calculateNutrition(List<String> ingredients, Integer servings);

    /**
     * 生成菜谱缩略图
     * 
     * @param imageUrl 原图URL
     * @return 缩略图URL
     */
    String generateThumbnail(String imageUrl);

    /**
     * 统计菜谱总数
     * 
     * @return 菜谱总数
     */
    Long countRecipes();

    /**
     * 统计已发布菜谱数
     * 
     * @return 已发布菜谱数
     */
    Long countPublishedRecipes();

    /**
     * 根据分类统计菜谱分布
     * 
     * @return 分类菜谱分布
     */
    List<Object> countRecipesByCategory();

    /**
     * 根据难度统计菜谱分布
     * 
     * @return 难度菜谱分布
     */
    List<Object> countRecipesByDifficulty();

    /**
     * 根据菜系统计菜谱分布
     * 
     * @return 菜系菜谱分布
     */
    List<Object> countRecipesByCuisine();

    /**
     * 获取热门菜谱（按浏览量排序）
     * 
     * @param limit 限制数量
     * @return 热门菜谱列表
     */
    List<Recipe> getPopularRecipes(Integer limit);

    /**
     * 获取最新菜谱
     * 
     * @param limit 限制数量
     * @return 最新菜谱列表
     */
    List<Recipe> getLatestRecipes(Integer limit);

    /**
     * 获取高评分菜谱
     * 
     * @param limit 限制数量
     * @param minRating 最小评分
     * @return 高评分菜谱列表
     */
    List<Recipe> getHighRatedRecipes(Integer limit, BigDecimal minRating);

    /**
     * 随机获取菜谱
     * 
     * @param limit 限制数量
     * @return 随机菜谱列表
     */
    List<Recipe> getRandomRecipes(Integer limit);

    /**
     * 获取相似菜谱
     * 
     * @param recipeId 菜谱ID
     * @param limit 限制数量
     * @return 相似菜谱列表
     */
    List<Recipe> getSimilarRecipes(Long recipeId, Integer limit);

    /**
     * 批量导入菜谱
     * 
     * @param recipes 菜谱列表
     * @return 导入结果
     */
    Boolean batchImportRecipes(List<Recipe> recipes);

    /**
     * 导出菜谱数据
     * 
     * @param recipeIds 菜谱ID列表
     * @return 菜谱数据
     */
    List<Recipe> exportRecipes(List<Long> recipeIds);

    /**
     * 清理无效菜谱数据
     * 
     * @param beforeTime 清理时间点
     * @return 清理数量
     */
    Integer cleanupInvalidRecipes(LocalDateTime beforeTime);

    /**
     * 同步菜谱统计数据
     * 
     * @return 是否同步成功
     */
    Boolean syncRecipeStatistics();
}