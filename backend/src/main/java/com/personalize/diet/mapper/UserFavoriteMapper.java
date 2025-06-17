package com.personalize.diet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.diet.entity.UserFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户收藏Mapper接口
 * 
 * @author PersonalizeDiet Team
 * @version 1.0.0
 */
@Mapper
public interface UserFavoriteMapper extends BaseMapper<UserFavorite> {

    /**
     * 根据用户ID查询收藏列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 收藏分页列表
     */
    @Select("SELECT * FROM user_favorites WHERE user_id = #{userId} AND deleted = 0 ORDER BY sort_weight DESC, create_time DESC")
    IPage<UserFavorite> findByUserId(Page<UserFavorite> page, @Param("userId") Long userId);

    /**
     * 根据用户ID和收藏类型查询收藏列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param favoriteType 收藏类型
     * @return 收藏分页列表
     */
    @Select("SELECT * FROM user_favorites WHERE user_id = #{userId} AND favorite_type = #{favoriteType} AND deleted = 0 ORDER BY sort_weight DESC, create_time DESC")
    IPage<UserFavorite> findByUserIdAndType(Page<UserFavorite> page, @Param("userId") Long userId, @Param("favoriteType") Integer favoriteType);

    /**
     * 根据用户ID和目标ID查询收藏
     * 
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param favoriteType 收藏类型
     * @return 收藏信息
     */
    @Select("SELECT * FROM user_favorites WHERE user_id = #{userId} AND target_id = #{targetId} AND favorite_type = #{favoriteType} AND deleted = 0")
    UserFavorite findByUserIdAndTargetId(@Param("userId") Long userId, @Param("targetId") Long targetId, @Param("favoriteType") Integer favoriteType);

    /**
     * 根据用户ID和分组ID查询收藏列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param groupId 分组ID
     * @return 收藏分页列表
     */
    @Select("SELECT * FROM user_favorites WHERE user_id = #{userId} AND group_id = #{groupId} AND deleted = 0 ORDER BY sort_weight DESC, create_time DESC")
    IPage<UserFavorite> findByUserIdAndGroupId(Page<UserFavorite> page, @Param("userId") Long userId, @Param("groupId") Long groupId);

    /**
     * 根据用户ID和分组名称查询收藏列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param groupName 分组名称
     * @return 收藏分页列表
     */
    @Select("SELECT * FROM user_favorites WHERE user_id = #{userId} AND group_name = #{groupName} AND deleted = 0 ORDER BY sort_weight DESC, create_time DESC")
    IPage<UserFavorite> findByUserIdAndGroupName(Page<UserFavorite> page, @Param("userId") Long userId, @Param("groupName") String groupName);

    /**
     * 根据用户ID和优先级查询收藏列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param priority 优先级
     * @return 收藏分页列表
     */
    @Select("SELECT * FROM user_favorites WHERE user_id = #{userId} AND priority = #{priority} AND deleted = 0 ORDER BY sort_weight DESC, create_time DESC")
    IPage<UserFavorite> findByUserIdAndPriority(Page<UserFavorite> page, @Param("userId") Long userId, @Param("priority") Integer priority);

    /**
     * 查询用户的公开收藏列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 公开收藏分页列表
     */
    @Select("SELECT * FROM user_favorites WHERE user_id = #{userId} AND is_public = 1 AND deleted = 0 ORDER BY sort_weight DESC, create_time DESC")
    IPage<UserFavorite> findPublicFavoritesByUserId(Page<UserFavorite> page, @Param("userId") Long userId);

    /**
     * 根据标签查询收藏列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param tags 标签列表
     * @return 收藏分页列表
     */
    IPage<UserFavorite> findByUserIdAndTags(Page<UserFavorite> page, @Param("userId") Long userId, @Param("tags") List<String> tags);

    /**
     * 搜索用户收藏
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param keyword 关键词
     * @return 收藏分页列表
     */
    IPage<UserFavorite> searchFavorites(Page<UserFavorite> page, @Param("userId") Long userId, @Param("keyword") String keyword);

    /**
     * 检查用户是否已收藏某个目标
     * 
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param favoriteType 收藏类型
     * @return 是否已收藏
     */
    @Select("SELECT COUNT(*) > 0 FROM user_favorites WHERE user_id = #{userId} AND target_id = #{targetId} AND favorite_type = #{favoriteType} AND deleted = 0")
    Boolean isFavorited(@Param("userId") Long userId, @Param("targetId") Long targetId, @Param("favoriteType") Integer favoriteType);

    /**
     * 更新收藏访问次数
     * 
     * @param favoriteId 收藏ID
     * @return 更新行数
     */
    @Update("UPDATE user_favorites SET access_count = access_count + 1, last_access_time = NOW() WHERE id = #{favoriteId}")
    int incrementAccessCount(@Param("favoriteId") Long favoriteId);

    /**
     * 更新收藏排序权重
     * 
     * @param favoriteId 收藏ID
     * @param sortWeight 排序权重
     * @return 更新行数
     */
    @Update("UPDATE user_favorites SET sort_weight = #{sortWeight} WHERE id = #{favoriteId}")
    int updateSortWeight(@Param("favoriteId") Long favoriteId, @Param("sortWeight") Integer sortWeight);

    /**
     * 更新收藏分组
     * 
     * @param favoriteId 收藏ID
     * @param groupId 分组ID
     * @param groupName 分组名称
     * @return 更新行数
     */
    @Update("UPDATE user_favorites SET group_id = #{groupId}, group_name = #{groupName} WHERE id = #{favoriteId}")
    int updateGroup(@Param("favoriteId") Long favoriteId, @Param("groupId") Long groupId, @Param("groupName") String groupName);

    /**
     * 更新收藏公开状态
     * 
     * @param favoriteId 收藏ID
     * @param isPublic 是否公开
     * @return 更新行数
     */
    @Update("UPDATE user_favorites SET is_public = #{isPublic} WHERE id = #{favoriteId}")
    int updatePublicStatus(@Param("favoriteId") Long favoriteId, @Param("isPublic") Boolean isPublic);

    /**
     * 更新收藏优先级
     * 
     * @param favoriteId 收藏ID
     * @param priority 优先级
     * @return 更新行数
     */
    @Update("UPDATE user_favorites SET priority = #{priority} WHERE id = #{favoriteId}")
    int updatePriority(@Param("favoriteId") Long favoriteId, @Param("priority") Integer priority);

    /**
     * 批量更新收藏分组
     * 
     * @param favoriteIds 收藏ID列表
     * @param groupId 分组ID
     * @param groupName 分组名称
     * @return 更新行数
     */
    int batchUpdateGroup(@Param("favoriteIds") List<Long> favoriteIds, @Param("groupId") Long groupId, @Param("groupName") String groupName);

    /**
     * 批量删除收藏
     * 
     * @param favoriteIds 收藏ID列表
     * @param userId 用户ID
     * @return 删除行数
     */
    @Update("UPDATE user_favorites SET deleted = 1 WHERE id IN (#{favoriteIds}) AND user_id = #{userId}")
    int batchDeleteFavorites(@Param("favoriteIds") List<Long> favoriteIds, @Param("userId") Long userId);

    /**
     * 统计用户收藏总数
     * 
     * @param userId 用户ID
     * @return 收藏总数
     */
    @Select("SELECT COUNT(*) FROM user_favorites WHERE user_id = #{userId} AND deleted = 0")
    Long countFavoritesByUserId(@Param("userId") Long userId);

    /**
     * 根据收藏类型统计用户收藏数
     * 
     * @param userId 用户ID
     * @param favoriteType 收藏类型
     * @return 收藏数
     */
    @Select("SELECT COUNT(*) FROM user_favorites WHERE user_id = #{userId} AND favorite_type = #{favoriteType} AND deleted = 0")
    Long countFavoritesByUserIdAndType(@Param("userId") Long userId, @Param("favoriteType") Integer favoriteType);

    /**
     * 统计目标被收藏次数
     * 
     * @param targetId 目标ID
     * @param favoriteType 收藏类型
     * @return 被收藏次数
     */
    @Select("SELECT COUNT(*) FROM user_favorites WHERE target_id = #{targetId} AND favorite_type = #{favoriteType} AND deleted = 0")
    Long countFavoritesByTarget(@Param("targetId") Long targetId, @Param("favoriteType") Integer favoriteType);

    /**
     * 统计收藏总数
     * 
     * @return 收藏总数
     */
    @Select("SELECT COUNT(*) FROM user_favorites WHERE deleted = 0")
    Long countFavorites();

    /**
     * 根据收藏类型统计收藏分布
     * 
     * @return 收藏类型分布
     */
    @Select("SELECT " +
            "CASE favorite_type " +
            "WHEN 1 THEN '菜谱' " +
            "WHEN 2 THEN '食材' " +
            "WHEN 3 THEN '聊天记录' " +
            "WHEN 4 THEN '其他' " +
            "ELSE '未知' " +
            "END as favorite_type_name, " +
            "COUNT(*) as count " +
            "FROM user_favorites WHERE deleted = 0 " +
            "GROUP BY favorite_type ORDER BY count DESC")
    List<Object> countFavoritesByType();

    /**
     * 根据优先级统计收藏分布
     * 
     * @return 优先级分布
     */
    @Select("SELECT " +
            "CASE priority " +
            "WHEN 1 THEN '低' " +
            "WHEN 2 THEN '中' " +
            "WHEN 3 THEN '高' " +
            "ELSE '未设置' " +
            "END as priority_name, " +
            "COUNT(*) as count " +
            "FROM user_favorites WHERE deleted = 0 " +
            "GROUP BY priority ORDER BY priority")
    List<Object> countFavoritesByPriority();

    /**
     * 获取用户收藏分组列表
     * 
     * @param userId 用户ID
     * @return 分组列表
     */
    @Select("SELECT DISTINCT group_id, group_name, COUNT(*) as count FROM user_favorites WHERE user_id = #{userId} AND group_name IS NOT NULL AND deleted = 0 GROUP BY group_id, group_name ORDER BY count DESC")
    List<Object> getFavoriteGroupsByUserId(@Param("userId") Long userId);

    /**
     * 获取热门收藏目标
     * 
     * @param favoriteType 收藏类型
     * @param limit 限制数量
     * @return 热门收藏目标列表
     */
    @Select("SELECT target_id, target_name, COUNT(*) as favorite_count FROM user_favorites WHERE favorite_type = #{favoriteType} AND deleted = 0 GROUP BY target_id, target_name ORDER BY favorite_count DESC LIMIT #{limit}")
    List<Object> getPopularFavoriteTargets(@Param("favoriteType") Integer favoriteType, @Param("limit") Integer limit);

    /**
     * 获取用户最近收藏
     * 
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 最近收藏列表
     */
    @Select("SELECT * FROM user_favorites WHERE user_id = #{userId} AND deleted = 0 ORDER BY create_time DESC LIMIT #{limit}")
    List<UserFavorite> getRecentFavoritesByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);

    /**
     * 获取用户最常访问的收藏
     * 
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 最常访问收藏列表
     */
    @Select("SELECT * FROM user_favorites WHERE user_id = #{userId} AND access_count > 0 AND deleted = 0 ORDER BY access_count DESC, last_access_time DESC LIMIT #{limit}")
    List<UserFavorite> getMostAccessedFavoritesByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);

    /**
     * 获取用户高优先级收藏
     * 
     * @param userId 用户ID
     * @return 高优先级收藏列表
     */
    @Select("SELECT * FROM user_favorites WHERE user_id = #{userId} AND priority = 3 AND deleted = 0 ORDER BY sort_weight DESC, create_time DESC")
    List<UserFavorite> getHighPriorityFavoritesByUserId(@Param("userId") Long userId);

    /**
     * 清理长期未访问的收藏
     * 
     * @param beforeTime 清理时间点
     * @param maxAccessCount 最大访问次数
     * @return 清理行数
     */
    @Update("UPDATE user_favorites SET deleted = 1 WHERE last_access_time < #{beforeTime} AND access_count <= #{maxAccessCount} AND priority = 1")
    int cleanupUnusedFavorites(@Param("beforeTime") LocalDateTime beforeTime, @Param("maxAccessCount") Integer maxAccessCount);
}