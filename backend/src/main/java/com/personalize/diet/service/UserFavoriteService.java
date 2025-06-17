package com.personalize.diet.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.diet.entity.UserFavorite;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户收藏服务接口
 * 
 * @author PersonalizeDiet Team
 * @version 1.0.0
 */
public interface UserFavoriteService {

    // ==================== 收藏管理 ====================

    /**
     * 添加收藏
     * 
     * @param userId 用户ID
     * @param favoriteType 收藏类型
     * @param targetId 收藏目标ID
     * @param targetName 收藏目标名称
     * @param targetDescription 收藏目标描述
     * @param targetImageUrl 收藏目标图片URL
     * @param groupId 收藏分组ID
     * @param tags 标签
     * @param notes 备注
     * @param priority 优先级
     * @param isPublic 是否公开
     * @return 创建的收藏
     */
    UserFavorite addFavorite(Long userId, Integer favoriteType, Long targetId, String targetName, 
                           String targetDescription, String targetImageUrl, Long groupId, 
                           String tags, String notes, Integer priority, Boolean isPublic);

    /**
     * 根据ID查询收藏
     * 
     * @param favoriteId 收藏ID
     * @return 收藏信息
     */
    UserFavorite getFavoriteById(Long favoriteId);

    /**
     * 检查是否已收藏
     * 
     * @param userId 用户ID
     * @param favoriteType 收藏类型
     * @param targetId 收藏目标ID
     * @return 是否已收藏
     */
    Boolean isFavorited(Long userId, Integer favoriteType, Long targetId);

    /**
     * 获取收藏记录
     * 
     * @param userId 用户ID
     * @param favoriteType 收藏类型
     * @param targetId 收藏目标ID
     * @return 收藏记录
     */
    UserFavorite getFavorite(Long userId, Integer favoriteType, Long targetId);

    /**
     * 更新收藏信息
     * 
     * @param favorite 收藏信息
     * @return 更新的收藏
     */
    UserFavorite updateFavorite(UserFavorite favorite);

    /**
     * 删除收藏
     * 
     * @param favoriteId 收藏ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    Boolean deleteFavorite(Long favoriteId, Long userId);

    /**
     * 取消收藏
     * 
     * @param userId 用户ID
     * @param favoriteType 收藏类型
     * @param targetId 收藏目标ID
     * @return 是否取消成功
     */
    Boolean unfavorite(Long userId, Integer favoriteType, Long targetId);

    /**
     * 切换收藏状态
     * 
     * @param userId 用户ID
     * @param favoriteType 收藏类型
     * @param targetId 收藏目标ID
     * @param targetName 收藏目标名称
     * @param targetDescription 收藏目标描述
     * @param targetImageUrl 收藏目标图片URL
     * @return 收藏状态（true-已收藏，false-已取消收藏）
     */
    Boolean toggleFavorite(Long userId, Integer favoriteType, Long targetId, String targetName, 
                          String targetDescription, String targetImageUrl);

    // ==================== 收藏查询 ====================

    /**
     * 根据用户ID查询收藏列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 收藏分页列表
     */
    IPage<UserFavorite> getFavoritesByUserId(Page<UserFavorite> page, Long userId);

    /**
     * 根据用户ID和收藏类型查询收藏列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param favoriteType 收藏类型
     * @return 收藏分页列表
     */
    IPage<UserFavorite> getFavoritesByUserIdAndType(Page<UserFavorite> page, Long userId, Integer favoriteType);

    /**
     * 根据分组查询收藏列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param groupId 分组ID
     * @return 收藏分页列表
     */
    IPage<UserFavorite> getFavoritesByGroupId(Page<UserFavorite> page, Long userId, Long groupId);

    /**
     * 根据分组名称查询收藏列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param groupName 分组名称
     * @return 收藏分页列表
     */
    IPage<UserFavorite> getFavoritesByGroupName(Page<UserFavorite> page, Long userId, String groupName);

    /**
     * 根据优先级查询收藏列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param priority 优先级
     * @return 收藏分页列表
     */
    IPage<UserFavorite> getFavoritesByPriority(Page<UserFavorite> page, Long userId, Integer priority);

    /**
     * 查询公开的收藏列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 公开收藏分页列表
     */
    IPage<UserFavorite> getPublicFavoritesByUserId(Page<UserFavorite> page, Long userId);

    /**
     * 根据标签查询收藏列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param tags 标签
     * @return 收藏分页列表
     */
    IPage<UserFavorite> getFavoritesByTags(Page<UserFavorite> page, Long userId, String tags);

    /**
     * 搜索收藏
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param keyword 关键词
     * @return 收藏分页列表
     */
    IPage<UserFavorite> searchFavorites(Page<UserFavorite> page, Long userId, String keyword);

    /**
     * 查询热门收藏
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param days 统计天数
     * @return 热门收藏分页列表
     */
    IPage<UserFavorite> getPopularFavorites(Page<UserFavorite> page, Long userId, Integer days);

    /**
     * 查询最近收藏
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param days 最近天数
     * @return 最近收藏分页列表
     */
    IPage<UserFavorite> getRecentFavorites(Page<UserFavorite> page, Long userId, Integer days);

    /**
     * 查询最常访问的收藏
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 最常访问收藏分页列表
     */
    IPage<UserFavorite> getMostAccessedFavorites(Page<UserFavorite> page, Long userId, Integer limit);

    /**
     * 查询高优先级收藏
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 高优先级收藏分页列表
     */
    IPage<UserFavorite> getHighPriorityFavorites(Page<UserFavorite> page, Long userId);

    // ==================== 收藏分组管理 ====================

    /**
     * 创建收藏分组
     * 
     * @param userId 用户ID
     * @param groupName 分组名称
     * @param description 分组描述
     * @return 分组ID
     */
    Long createFavoriteGroup(Long userId, String groupName, String description);

    /**
     * 获取用户的收藏分组列表
     * 
     * @param userId 用户ID
     * @return 分组列表
     */
    List<Map<String, Object>> getFavoriteGroupsByUserId(Long userId);

    /**
     * 更新收藏分组
     * 
     * @param groupId 分组ID
     * @param userId 用户ID
     * @param groupName 分组名称
     * @param description 分组描述
     * @return 是否更新成功
     */
    Boolean updateFavoriteGroup(Long groupId, Long userId, String groupName, String description);

    /**
     * 删除收藏分组
     * 
     * @param groupId 分组ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    Boolean deleteFavoriteGroup(Long groupId, Long userId);

    /**
     * 移动收藏到分组
     * 
     * @param favoriteId 收藏ID
     * @param userId 用户ID
     * @param groupId 分组ID
     * @return 是否移动成功
     */
    Boolean moveFavoriteToGroup(Long favoriteId, Long userId, Long groupId);

    /**
     * 批量移动收藏到分组
     * 
     * @param favoriteIds 收藏ID列表
     * @param userId 用户ID
     * @param groupId 分组ID
     * @return 是否移动成功
     */
    Boolean batchMoveFavoritesToGroup(List<Long> favoriteIds, Long userId, Long groupId);

    // ==================== 收藏操作 ====================

    /**
     * 更新收藏访问次数
     * 
     * @param favoriteId 收藏ID
     * @return 是否更新成功
     */
    Boolean incrementAccessCount(Long favoriteId);

    /**
     * 更新收藏排序权重
     * 
     * @param favoriteId 收藏ID
     * @param userId 用户ID
     * @param sortWeight 排序权重
     * @return 是否更新成功
     */
    Boolean updateSortWeight(Long favoriteId, Long userId, Integer sortWeight);

    /**
     * 更新收藏公开状态
     * 
     * @param favoriteId 收藏ID
     * @param userId 用户ID
     * @param isPublic 是否公开
     * @return 是否更新成功
     */
    Boolean updatePublicStatus(Long favoriteId, Long userId, Boolean isPublic);

    /**
     * 更新收藏优先级
     * 
     * @param favoriteId 收藏ID
     * @param userId 用户ID
     * @param priority 优先级
     * @return 是否更新成功
     */
    Boolean updatePriority(Long favoriteId, Long userId, Integer priority);

    /**
     * 更新收藏备注
     * 
     * @param favoriteId 收藏ID
     * @param userId 用户ID
     * @param notes 备注
     * @return 是否更新成功
     */
    Boolean updateNotes(Long favoriteId, Long userId, String notes);

    /**
     * 更新收藏标签
     * 
     * @param favoriteId 收藏ID
     * @param userId 用户ID
     * @param tags 标签
     * @return 是否更新成功
     */
    Boolean updateTags(Long favoriteId, Long userId, String tags);

    /**
     * 批量删除收藏
     * 
     * @param favoriteIds 收藏ID列表
     * @param userId 用户ID
     * @return 是否删除成功
     */
    Boolean batchDeleteFavorites(List<Long> favoriteIds, Long userId);

    /**
     * 批量更新收藏分组
     * 
     * @param favoriteIds 收藏ID列表
     * @param userId 用户ID
     * @param groupId 分组ID
     * @return 是否更新成功
     */
    Boolean batchUpdateFavoriteGroup(List<Long> favoriteIds, Long userId, Long groupId);

    // ==================== 统计分析 ====================

    /**
     * 统计用户收藏总数
     * 
     * @param userId 用户ID
     * @return 收藏总数
     */
    Long countFavoritesByUserId(Long userId);

    /**
     * 根据收藏类型统计用户收藏数
     * 
     * @param userId 用户ID
     * @param favoriteType 收藏类型
     * @return 收藏数
     */
    Long countFavoritesByUserIdAndType(Long userId, Integer favoriteType);

    /**
     * 统计目标被收藏次数
     * 
     * @param favoriteType 收藏类型
     * @param targetId 目标ID
     * @return 被收藏次数
     */
    Long countFavoritesByTarget(Integer favoriteType, Long targetId);

    /**
     * 统计总收藏数
     * 
     * @return 总收藏数
     */
    Long countTotalFavorites();

    /**
     * 根据收藏类型统计收藏分布
     * 
     * @return 收藏类型分布
     */
    List<Object> countFavoritesByType();

    /**
     * 根据优先级统计收藏分布
     * 
     * @param userId 用户ID
     * @return 优先级分布
     */
    List<Object> countFavoritesByPriority(Long userId);

    /**
     * 获取热门收藏目标
     * 
     * @param favoriteType 收藏类型
     * @param limit 限制数量
     * @return 热门收藏目标列表
     */
    List<Object> getPopularFavoriteTargets(Integer favoriteType, Integer limit);

    /**
     * 获取用户收藏统计
     * 
     * @param userId 用户ID
     * @return 收藏统计
     */
    Map<String, Object> getUserFavoriteStats(Long userId);

    /**
     * 获取收藏趋势统计
     * 
     * @param userId 用户ID
     * @param days 统计天数
     * @return 收藏趋势
     */
    List<Object> getFavoriteTrends(Long userId, Integer days);

    // ==================== 推荐系统 ====================

    /**
     * 根据用户收藏推荐相似内容
     * 
     * @param userId 用户ID
     * @param favoriteType 收藏类型
     * @param limit 推荐数量
     * @return 推荐内容列表
     */
    List<Object> recommendSimilarContent(Long userId, Integer favoriteType, Integer limit);

    /**
     * 根据用户收藏历史推荐新内容
     * 
     * @param userId 用户ID
     * @param limit 推荐数量
     * @return 推荐内容列表
     */
    List<Object> recommendNewContent(Long userId, Integer limit);

    /**
     * 获取用户可能感兴趣的标签
     * 
     * @param userId 用户ID
     * @param limit 标签数量
     * @return 推荐标签列表
     */
    List<String> getRecommendedTags(Long userId, Integer limit);

    // ==================== 数据管理 ====================

    /**
     * 导出用户收藏数据
     * 
     * @param userId 用户ID
     * @param favoriteType 收藏类型（可选）
     * @return 收藏数据
     */
    List<UserFavorite> exportUserFavorites(Long userId, Integer favoriteType);

    /**
     * 导入用户收藏数据
     * 
     * @param userId 用户ID
     * @param favorites 收藏数据
     * @return 导入成功数量
     */
    Integer importUserFavorites(Long userId, List<UserFavorite> favorites);

    /**
     * 清理长期未访问的收藏
     * 
     * @param beforeTime 清理时间点
     * @return 清理数量
     */
    Integer cleanupUnusedFavorites(LocalDateTime beforeTime);

    /**
     * 同步收藏目标信息
     * 
     * @param favoriteType 收藏类型
     * @param targetId 目标ID
     * @param targetName 目标名称
     * @param targetDescription 目标描述
     * @param targetImageUrl 目标图片URL
     * @return 同步成功数量
     */
    Integer syncFavoriteTargetInfo(Integer favoriteType, Long targetId, String targetName, 
                                  String targetDescription, String targetImageUrl);

    /**
     * 备份用户收藏数据
     * 
     * @param userId 用户ID
     * @param backupPath 备份路径
     * @return 是否备份成功
     */
    Boolean backupUserFavorites(Long userId, String backupPath);

    /**
     * 恢复用户收藏数据
     * 
     * @param userId 用户ID
     * @param backupPath 备份路径
     * @return 是否恢复成功
     */
    Boolean restoreUserFavorites(Long userId, String backupPath);
}