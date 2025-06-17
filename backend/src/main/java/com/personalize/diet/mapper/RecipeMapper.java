package com.personalize.diet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.diet.entity.Recipe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * 菜谱Mapper接口
 * 
 * @author PersonalizeDiet Team
 * @version 1.0.0
 */
@Mapper
public interface RecipeMapper extends BaseMapper<Recipe> {

    /**
     * 根据菜谱名称查询菜谱
     * 
     * @param name 菜谱名称
     * @return 菜谱信息
     */
    @Select("SELECT * FROM recipes WHERE name = #{name} AND deleted = 0")
    Recipe findByName(@Param("name") String name);

    /**
     * 根据分类ID查询菜谱列表
     * 
     * @param page 分页参数
     * @param categoryId 分类ID
     * @return 菜谱分页列表
     */
    @Select("SELECT * FROM recipes WHERE category_id = #{categoryId} AND status = 1 AND deleted = 0 ORDER BY sort_weight DESC, create_time DESC")
    IPage<Recipe> findByCategoryId(Page<Recipe> page, @Param("categoryId") Long categoryId);

    /**
     * 根据菜系类型查询菜谱列表
     * 
     * @param page 分页参数
     * @param cuisineType 菜系类型
     * @return 菜谱分页列表
     */
    @Select("SELECT * FROM recipes WHERE cuisine_type = #{cuisineType} AND status = 1 AND deleted = 0 ORDER BY sort_weight DESC, create_time DESC")
    IPage<Recipe> findByCuisineType(Page<Recipe> page, @Param("cuisineType") Integer cuisineType);

    /**
     * 根据难度等级查询菜谱列表
     * 
     * @param page 分页参数
     * @param difficulty 难度等级
     * @return 菜谱分页列表
     */
    @Select("SELECT * FROM recipes WHERE difficulty = #{difficulty} AND status = 1 AND deleted = 0 ORDER BY sort_weight DESC, create_time DESC")
    IPage<Recipe> findByDifficulty(Page<Recipe> page, @Param("difficulty") Integer difficulty);

    /**
     * 根据烹饪时间范围查询菜谱列表
     * 
     * @param page 分页参数
     * @param minTime 最小时间
     * @param maxTime 最大时间
     * @return 菜谱分页列表
     */
    @Select("SELECT * FROM recipes WHERE cook_time BETWEEN #{minTime} AND #{maxTime} AND status = 1 AND deleted = 0 ORDER BY sort_weight DESC, create_time DESC")
    IPage<Recipe> findByCookTimeRange(Page<Recipe> page, @Param("minTime") Integer minTime, @Param("maxTime") Integer maxTime);

    /**
     * 根据热量范围查询菜谱列表
     * 
     * @param page 分页参数
     * @param minCalories 最小热量
     * @param maxCalories 最大热量
     * @return 菜谱分页列表
     */
    @Select("SELECT * FROM recipes WHERE calories_per_serving BETWEEN #{minCalories} AND #{maxCalories} AND status = 1 AND deleted = 0 ORDER BY sort_weight DESC, create_time DESC")
    IPage<Recipe> findByCaloriesRange(Page<Recipe> page, @Param("minCalories") BigDecimal minCalories, @Param("maxCalories") BigDecimal maxCalories);

    /**
     * 根据作者ID查询菜谱列表
     * 
     * @param page 分页参数
     * @param authorId 作者ID
     * @return 菜谱分页列表
     */
    @Select("SELECT * FROM recipes WHERE author_id = #{authorId} AND deleted = 0 ORDER BY create_time DESC")
    IPage<Recipe> findByAuthorId(Page<Recipe> page, @Param("authorId") Long authorId);

    /**
     * 查询推荐菜谱列表
     * 
     * @param page 分页参数
     * @return 菜谱分页列表
     */
    @Select("SELECT * FROM recipes WHERE is_recommended = 1 AND status = 1 AND deleted = 0 ORDER BY sort_weight DESC, create_time DESC")
    IPage<Recipe> findRecommendedRecipes(Page<Recipe> page);

    /**
     * 查询热门菜谱列表
     * 
     * @param page 分页参数
     * @return 菜谱分页列表
     */
    @Select("SELECT * FROM recipes WHERE is_hot = 1 AND status = 1 AND deleted = 0 ORDER BY view_count DESC, sort_weight DESC")
    IPage<Recipe> findHotRecipes(Page<Recipe> page);

    /**
     * 查询精选菜谱列表
     * 
     * @param page 分页参数
     * @return 菜谱分页列表
     */
    @Select("SELECT * FROM recipes WHERE is_featured = 1 AND status = 1 AND deleted = 0 ORDER BY sort_weight DESC, create_time DESC")
    IPage<Recipe> findFeaturedRecipes(Page<Recipe> page);

    /**
     * 根据评分范围查询菜谱列表
     * 
     * @param page 分页参数
     * @param minRating 最小评分
     * @param maxRating 最大评分
     * @return 菜谱分页列表
     */
    @Select("SELECT * FROM recipes WHERE average_rating BETWEEN #{minRating} AND #{maxRating} AND status = 1 AND deleted = 0 ORDER BY average_rating DESC, sort_weight DESC")
    IPage<Recipe> findByRatingRange(Page<Recipe> page, @Param("minRating") BigDecimal minRating, @Param("maxRating") BigDecimal maxRating);

    /**
     * 根据标签查询菜谱列表
     * 
     * @param page 分页参数
     * @param tags 标签列表
     * @return 菜谱分页列表
     */
    IPage<Recipe> findByTags(Page<Recipe> page, @Param("tags") List<String> tags);

    /**
     * 根据食材查询菜谱列表
     * 
     * @param page 分页参数
     * @param ingredients 食材列表
     * @return 菜谱分页列表
     */
    IPage<Recipe> findByIngredients(Page<Recipe> page, @Param("ingredients") List<String> ingredients);

    /**
     * 搜索菜谱
     * 
     * @param page 分页参数
     * @param keyword 关键词
     * @return 菜谱分页列表
     */
    IPage<Recipe> searchRecipes(Page<Recipe> page, @Param("keyword") String keyword);

    /**
     * 更新菜谱浏览次数
     * 
     * @param recipeId 菜谱ID
     * @return 更新行数
     */
    @Update("UPDATE recipes SET view_count = view_count + 1 WHERE id = #{recipeId}")
    int incrementViewCount(@Param("recipeId") Long recipeId);

    /**
     * 更新菜谱收藏次数
     * 
     * @param recipeId 菜谱ID
     * @param increment 增量（1或-1）
     * @return 更新行数
     */
    @Update("UPDATE recipes SET favorite_count = favorite_count + #{increment} WHERE id = #{recipeId}")
    int updateFavoriteCount(@Param("recipeId") Long recipeId, @Param("increment") Integer increment);

    /**
     * 更新菜谱点赞次数
     * 
     * @param recipeId 菜谱ID
     * @param increment 增量（1或-1）
     * @return 更新行数
     */
    @Update("UPDATE recipes SET like_count = like_count + #{increment} WHERE id = #{recipeId}")
    int updateLikeCount(@Param("recipeId") Long recipeId, @Param("increment") Integer increment);

    /**
     * 更新菜谱评论次数
     * 
     * @param recipeId 菜谱ID
     * @param increment 增量（1或-1）
     * @return 更新行数
     */
    @Update("UPDATE recipes SET comment_count = comment_count + #{increment} WHERE id = #{recipeId}")
    int updateCommentCount(@Param("recipeId") Long recipeId, @Param("increment") Integer increment);

    /**
     * 更新菜谱制作次数
     * 
     * @param recipeId 菜谱ID
     * @return 更新行数
     */
    @Update("UPDATE recipes SET made_count = made_count + 1 WHERE id = #{recipeId}")
    int incrementMadeCount(@Param("recipeId") Long recipeId);

    /**
     * 更新菜谱分享次数
     * 
     * @param recipeId 菜谱ID
     * @return 更新行数
     */
    @Update("UPDATE recipes SET share_count = share_count + 1 WHERE id = #{recipeId}")
    int incrementShareCount(@Param("recipeId") Long recipeId);

    /**
     * 更新菜谱平均评分
     * 
     * @param recipeId 菜谱ID
     * @param averageRating 平均评分
     * @param ratingCount 评分次数
     * @return 更新行数
     */
    @Update("UPDATE recipes SET average_rating = #{averageRating}, rating_count = #{ratingCount} WHERE id = #{recipeId}")
    int updateRating(@Param("recipeId") Long recipeId, @Param("averageRating") BigDecimal averageRating, @Param("ratingCount") Integer ratingCount);

    /**
     * 统计菜谱总数
     * 
     * @return 菜谱总数
     */
    @Select("SELECT COUNT(*) FROM recipes WHERE deleted = 0")
    Long countRecipes();

    /**
     * 统计已发布菜谱数
     * 
     * @return 已发布菜谱数
     */
    @Select("SELECT COUNT(*) FROM recipes WHERE status = 1 AND deleted = 0")
    Long countPublishedRecipes();

    /**
     * 根据分类统计菜谱分布
     * 
     * @return 分类菜谱分布
     */
    @Select("SELECT category_name, COUNT(*) as count FROM recipes WHERE status = 1 AND deleted = 0 GROUP BY category_id, category_name ORDER BY count DESC")
    List<Object> countRecipesByCategory();

    /**
     * 根据难度统计菜谱分布
     * 
     * @return 难度菜谱分布
     */
    @Select("SELECT " +
            "CASE difficulty " +
            "WHEN 1 THEN '简单' " +
            "WHEN 2 THEN '普通' " +
            "WHEN 3 THEN '困难' " +
            "ELSE '未知' " +
            "END as difficulty_name, " +
            "COUNT(*) as count " +
            "FROM recipes WHERE status = 1 AND deleted = 0 " +
            "GROUP BY difficulty ORDER BY difficulty")
    List<Object> countRecipesByDifficulty();

    /**
     * 根据菜系统计菜谱分布
     * 
     * @return 菜系菜谱分布
     */
    @Select("SELECT " +
            "CASE cuisine_type " +
            "WHEN 1 THEN '川菜' " +
            "WHEN 2 THEN '粤菜' " +
            "WHEN 3 THEN '鲁菜' " +
            "WHEN 4 THEN '苏菜' " +
            "WHEN 5 THEN '浙菜' " +
            "WHEN 6 THEN '闽菜' " +
            "WHEN 7 THEN '湘菜' " +
            "WHEN 8 THEN '徽菜' " +
            "WHEN 9 THEN '其他' " +
            "ELSE '未知' " +
            "END as cuisine_name, " +
            "COUNT(*) as count " +
            "FROM recipes WHERE status = 1 AND deleted = 0 " +
            "GROUP BY cuisine_type ORDER BY count DESC")
    List<Object> countRecipesByCuisine();

    /**
     * 获取热门菜谱（按浏览量排序）
     * 
     * @param limit 限制数量
     * @return 热门菜谱列表
     */
    @Select("SELECT * FROM recipes WHERE status = 1 AND deleted = 0 ORDER BY view_count DESC LIMIT #{limit}")
    List<Recipe> getPopularRecipes(@Param("limit") Integer limit);

    /**
     * 获取最新菜谱
     * 
     * @param limit 限制数量
     * @return 最新菜谱列表
     */
    @Select("SELECT * FROM recipes WHERE status = 1 AND deleted = 0 ORDER BY create_time DESC LIMIT #{limit}")
    List<Recipe> getLatestRecipes(@Param("limit") Integer limit);

    /**
     * 获取高评分菜谱
     * 
     * @param limit 限制数量
     * @param minRating 最小评分
     * @return 高评分菜谱列表
     */
    @Select("SELECT * FROM recipes WHERE status = 1 AND deleted = 0 AND average_rating >= #{minRating} ORDER BY average_rating DESC, rating_count DESC LIMIT #{limit}")
    List<Recipe> getHighRatedRecipes(@Param("limit") Integer limit, @Param("minRating") BigDecimal minRating);

    /**
     * 随机获取菜谱
     * 
     * @param limit 限制数量
     * @return 随机菜谱列表
     */
    @Select("SELECT * FROM recipes WHERE status = 1 AND deleted = 0 ORDER BY RAND() LIMIT #{limit}")
    List<Recipe> getRandomRecipes(@Param("limit") Integer limit);
}