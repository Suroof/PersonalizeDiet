package com.personalize.diet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.diet.entity.ChatSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 聊天会话Mapper接口
 * 
 * @author PersonalizeDiet Team
 * @version 1.0.0
 */
@Mapper
public interface ChatSessionMapper extends BaseMapper<ChatSession> {

    /**
     * 根据用户ID查询会话列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 会话分页列表
     */
    @Select("SELECT * FROM chat_sessions WHERE user_id = #{userId} AND deleted = 0 ORDER BY is_pinned DESC, last_message_time DESC")
    IPage<ChatSession> findByUserId(Page<ChatSession> page, @Param("userId") Long userId);

    /**
     * 根据Dify会话ID查询会话
     * 
     * @param difySessionId Dify会话ID
     * @return 会话信息
     */
    @Select("SELECT * FROM chat_sessions WHERE dify_session_id = #{difySessionId} AND deleted = 0")
    ChatSession findByDifySessionId(@Param("difySessionId") String difySessionId);

    /**
     * 根据Dify对话ID查询会话
     * 
     * @param difyConversationId Dify对话ID
     * @return 会话信息
     */
    @Select("SELECT * FROM chat_sessions WHERE dify_conversation_id = #{difyConversationId} AND deleted = 0")
    ChatSession findByDifyConversationId(@Param("difyConversationId") String difyConversationId);

    /**
     * 根据会话类型查询会话列表
     * 
     * @param page 分页参数
     * @param sessionType 会话类型
     * @return 会话分页列表
     */
    @Select("SELECT * FROM chat_sessions WHERE session_type = #{sessionType} AND deleted = 0 ORDER BY create_time DESC")
    IPage<ChatSession> findBySessionType(Page<ChatSession> page, @Param("sessionType") Integer sessionType);

    /**
     * 根据状态查询会话列表
     * 
     * @param page 分页参数
     * @param status 状态
     * @return 会话分页列表
     */
    @Select("SELECT * FROM chat_sessions WHERE status = #{status} AND deleted = 0 ORDER BY create_time DESC")
    IPage<ChatSession> findByStatus(Page<ChatSession> page, @Param("status") Integer status);

    /**
     * 查询用户的活跃会话
     * 
     * @param userId 用户ID
     * @param hours 活跃时间范围（小时）
     * @return 活跃会话列表
     */
    @Select("SELECT * FROM chat_sessions WHERE user_id = #{userId} AND status = 1 AND last_message_time >= DATE_SUB(NOW(), INTERVAL #{hours} HOUR) AND deleted = 0 ORDER BY last_message_time DESC")
    List<ChatSession> findActiveSessionsByUserId(@Param("userId") Long userId, @Param("hours") Integer hours);

    /**
     * 查询用户的置顶会话
     * 
     * @param userId 用户ID
     * @return 置顶会话列表
     */
    @Select("SELECT * FROM chat_sessions WHERE user_id = #{userId} AND is_pinned = 1 AND deleted = 0 ORDER BY last_message_time DESC")
    List<ChatSession> findPinnedSessionsByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的收藏会话
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 收藏会话分页列表
     */
    @Select("SELECT * FROM chat_sessions WHERE user_id = #{userId} AND is_favorite = 1 AND deleted = 0 ORDER BY last_message_time DESC")
    IPage<ChatSession> findFavoriteSessionsByUserId(Page<ChatSession> page, @Param("userId") Long userId);

    /**
     * 根据标签查询会话列表
     * 
     * @param page 分页参数
     * @param tags 标签列表
     * @return 会话分页列表
     */
    IPage<ChatSession> findByTags(Page<ChatSession> page, @Param("tags") List<String> tags);

    /**
     * 搜索会话
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param keyword 关键词
     * @return 会话分页列表
     */
    IPage<ChatSession> searchSessions(Page<ChatSession> page, @Param("userId") Long userId, @Param("keyword") String keyword);

    /**
     * 更新会话最后消息信息
     * 
     * @param sessionId 会话ID
     * @param lastMessage 最后消息内容
     * @param lastMessageTime 最后消息时间
     * @param lastMessageSender 最后消息发送者
     * @return 更新行数
     */
    @Update("UPDATE chat_sessions SET last_message = #{lastMessage}, last_message_time = #{lastMessageTime}, last_message_sender = #{lastMessageSender}, message_count = message_count + 1 WHERE id = #{sessionId}")
    int updateLastMessage(@Param("sessionId") Long sessionId, @Param("lastMessage") String lastMessage, @Param("lastMessageTime") LocalDateTime lastMessageTime, @Param("lastMessageSender") String lastMessageSender);

    /**
     * 更新会话消息总数
     * 
     * @param sessionId 会话ID
     * @param increment 增量（1或-1）
     * @return 更新行数
     */
    @Update("UPDATE chat_sessions SET message_count = message_count + #{increment} WHERE id = #{sessionId}")
    int updateMessageCount(@Param("sessionId") Long sessionId, @Param("increment") Integer increment);

    /**
     * 更新会话状态
     * 
     * @param sessionId 会话ID
     * @param status 状态
     * @return 更新行数
     */
    @Update("UPDATE chat_sessions SET status = #{status} WHERE id = #{sessionId}")
    int updateStatus(@Param("sessionId") Long sessionId, @Param("status") Integer status);

    /**
     * 更新会话置顶状态
     * 
     * @param sessionId 会话ID
     * @param isPinned 是否置顶
     * @return 更新行数
     */
    @Update("UPDATE chat_sessions SET is_pinned = #{isPinned} WHERE id = #{sessionId}")
    int updatePinnedStatus(@Param("sessionId") Long sessionId, @Param("isPinned") Boolean isPinned);

    /**
     * 更新会话收藏状态
     * 
     * @param sessionId 会话ID
     * @param isFavorite 是否收藏
     * @return 更新行数
     */
    @Update("UPDATE chat_sessions SET is_favorite = #{isFavorite} WHERE id = #{sessionId}")
    int updateFavoriteStatus(@Param("sessionId") Long sessionId, @Param("isFavorite") Boolean isFavorite);

    /**
     * 更新会话评分
     * 
     * @param sessionId 会话ID
     * @param rating 评分
     * @return 更新行数
     */
    @Update("UPDATE chat_sessions SET rating = #{rating} WHERE id = #{sessionId}")
    int updateRating(@Param("sessionId") Long sessionId, @Param("rating") Integer rating);

    /**
     * 更新会话过期时间
     * 
     * @param sessionId 会话ID
     * @param expireTime 过期时间
     * @return 更新行数
     */
    @Update("UPDATE chat_sessions SET expire_time = #{expireTime} WHERE id = #{sessionId}")
    int updateExpireTime(@Param("sessionId") Long sessionId, @Param("expireTime") LocalDateTime expireTime);

    /**
     * 查询过期的会话
     * 
     * @param currentTime 当前时间
     * @return 过期会话列表
     */
    @Select("SELECT * FROM chat_sessions WHERE expire_time IS NOT NULL AND expire_time < #{currentTime} AND status != 3 AND deleted = 0")
    List<ChatSession> findExpiredSessions(@Param("currentTime") LocalDateTime currentTime);

    /**
     * 统计用户会话总数
     * 
     * @param userId 用户ID
     * @return 会话总数
     */
    @Select("SELECT COUNT(*) FROM chat_sessions WHERE user_id = #{userId} AND deleted = 0")
    Long countSessionsByUserId(@Param("userId") Long userId);

    /**
     * 统计活跃会话数
     * 
     * @param hours 活跃时间范围（小时）
     * @return 活跃会话数
     */
    @Select("SELECT COUNT(*) FROM chat_sessions WHERE status = 1 AND last_message_time >= DATE_SUB(NOW(), INTERVAL #{hours} HOUR) AND deleted = 0")
    Long countActiveSessions(@Param("hours") Integer hours);

    /**
     * 统计会话总数
     * 
     * @return 会话总数
     */
    @Select("SELECT COUNT(*) FROM chat_sessions WHERE deleted = 0")
    Long countSessions();

    /**
     * 根据会话类型统计会话分布
     * 
     * @return 会话类型分布
     */
    @Select("SELECT " +
            "CASE session_type " +
            "WHEN 1 THEN '菜谱推荐' " +
            "WHEN 2 THEN '营养咨询' " +
            "WHEN 3 THEN '烹饪指导' " +
            "WHEN 4 THEN '食材搭配' " +
            "WHEN 5 THEN '其他' " +
            "ELSE '未知' " +
            "END as session_type_name, " +
            "COUNT(*) as count " +
            "FROM chat_sessions WHERE deleted = 0 " +
            "GROUP BY session_type ORDER BY count DESC")
    List<Object> countSessionsByType();

    /**
     * 根据状态统计会话分布
     * 
     * @return 会话状态分布
     */
    @Select("SELECT " +
            "CASE status " +
            "WHEN 1 THEN '活跃' " +
            "WHEN 2 THEN '暂停' " +
            "WHEN 3 THEN '结束' " +
            "ELSE '未知' " +
            "END as status_name, " +
            "COUNT(*) as count " +
            "FROM chat_sessions WHERE deleted = 0 " +
            "GROUP BY status ORDER BY status")
    List<Object> countSessionsByStatus();

    /**
     * 获取用户最近的会话
     * 
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 最近会话列表
     */
    @Select("SELECT * FROM chat_sessions WHERE user_id = #{userId} AND deleted = 0 ORDER BY last_message_time DESC LIMIT #{limit}")
    List<ChatSession> getRecentSessionsByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);

    /**
     * 获取热门会话（按消息数排序）
     * 
     * @param limit 限制数量
     * @return 热门会话列表
     */
    @Select("SELECT * FROM chat_sessions WHERE deleted = 0 ORDER BY message_count DESC, last_message_time DESC LIMIT #{limit}")
    List<ChatSession> getPopularSessions(@Param("limit") Integer limit);

    /**
     * 获取高评分会话
     * 
     * @param limit 限制数量
     * @param minRating 最小评分
     * @return 高评分会话列表
     */
    @Select("SELECT * FROM chat_sessions WHERE rating >= #{minRating} AND deleted = 0 ORDER BY rating DESC, last_message_time DESC LIMIT #{limit}")
    List<ChatSession> getHighRatedSessions(@Param("limit") Integer limit, @Param("minRating") Integer minRating);

    /**
     * 批量更新会话状态为过期
     * 
     * @param sessionIds 会话ID列表
     * @return 更新行数
     */
    int batchUpdateExpiredStatus(@Param("sessionIds") List<Long> sessionIds);

    /**
     * 清理过期会话数据
     * 
     * @param beforeTime 清理时间点
     * @return 清理行数
     */
    @Update("UPDATE chat_sessions SET deleted = 1 WHERE expire_time < #{beforeTime} AND status = 3")
    int cleanupExpiredSessions(@Param("beforeTime") LocalDateTime beforeTime);
}