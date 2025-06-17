package com.personalize.diet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.diet.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 聊天消息Mapper接口
 * 
 * @author PersonalizeDiet Team
 * @version 1.0.0
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

    /**
     * 根据会话ID查询消息列表
     * 
     * @param page 分页参数
     * @param sessionId 会话ID
     * @return 消息分页列表
     */
    @Select("SELECT * FROM chat_messages WHERE session_id = #{sessionId} AND deleted = 0 ORDER BY sequence_number ASC, create_time ASC")
    IPage<ChatMessage> findBySessionId(Page<ChatMessage> page, @Param("sessionId") Long sessionId);

    /**
     * 根据用户ID查询消息列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 消息分页列表
     */
    @Select("SELECT * FROM chat_messages WHERE user_id = #{userId} AND deleted = 0 ORDER BY create_time DESC")
    IPage<ChatMessage> findByUserId(Page<ChatMessage> page, @Param("userId") Long userId);

    /**
     * 根据Dify消息ID查询消息
     * 
     * @param difyMessageId Dify消息ID
     * @return 消息信息
     */
    @Select("SELECT * FROM chat_messages WHERE dify_message_id = #{difyMessageId} AND deleted = 0")
    ChatMessage findByDifyMessageId(@Param("difyMessageId") String difyMessageId);

    /**
     * 根据父消息ID查询子消息列表
     * 
     * @param parentMessageId 父消息ID
     * @return 子消息列表
     */
    @Select("SELECT * FROM chat_messages WHERE parent_message_id = #{parentMessageId} AND deleted = 0 ORDER BY create_time ASC")
    List<ChatMessage> findByParentMessageId(@Param("parentMessageId") Long parentMessageId);

    /**
     * 根据消息类型查询消息列表
     * 
     * @param page 分页参数
     * @param messageType 消息类型
     * @return 消息分页列表
     */
    @Select("SELECT * FROM chat_messages WHERE message_type = #{messageType} AND deleted = 0 ORDER BY create_time DESC")
    IPage<ChatMessage> findByMessageType(Page<ChatMessage> page, @Param("messageType") Integer messageType);

    /**
     * 根据发送者类型查询消息列表
     * 
     * @param page 分页参数
     * @param senderType 发送者类型
     * @return 消息分页列表
     */
    @Select("SELECT * FROM chat_messages WHERE sender_type = #{senderType} AND deleted = 0 ORDER BY create_time DESC")
    IPage<ChatMessage> findBySenderType(Page<ChatMessage> page, @Param("senderType") Integer senderType);

    /**
     * 根据消息状态查询消息列表
     * 
     * @param page 分页参数
     * @param status 消息状态
     * @return 消息分页列表
     */
    @Select("SELECT * FROM chat_messages WHERE status = #{status} AND deleted = 0 ORDER BY create_time DESC")
    IPage<ChatMessage> findByStatus(Page<ChatMessage> page, @Param("status") Integer status);

    /**
     * 查询会话中的最后一条消息
     * 
     * @param sessionId 会话ID
     * @return 最后一条消息
     */
    @Select("SELECT * FROM chat_messages WHERE session_id = #{sessionId} AND deleted = 0 ORDER BY sequence_number DESC, create_time DESC LIMIT 1")
    ChatMessage findLastMessageBySessionId(@Param("sessionId") Long sessionId);

    /**
     * 查询会话中用户的最后一条消息
     * 
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 用户最后一条消息
     */
    @Select("SELECT * FROM chat_messages WHERE session_id = #{sessionId} AND user_id = #{userId} AND sender_type = 1 AND deleted = 0 ORDER BY create_time DESC LIMIT 1")
    ChatMessage findLastUserMessageBySessionId(@Param("sessionId") Long sessionId, @Param("userId") Long userId);

    /**
     * 查询会话中AI的最后一条消息
     * 
     * @param sessionId 会话ID
     * @return AI最后一条消息
     */
    @Select("SELECT * FROM chat_messages WHERE session_id = #{sessionId} AND sender_type = 2 AND deleted = 0 ORDER BY create_time DESC LIMIT 1")
    ChatMessage findLastAiMessageBySessionId(@Param("sessionId") Long sessionId);

    /**
     * 查询流式消息的所有片段
     * 
     * @param streamId 流式消息ID
     * @return 流式消息片段列表
     */
    @Select("SELECT * FROM chat_messages WHERE stream_id = #{streamId} AND deleted = 0 ORDER BY stream_sequence ASC")
    List<ChatMessage> findStreamMessagesByStreamId(@Param("streamId") String streamId);

    /**
     * 查询用户的收藏消息
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 收藏消息分页列表
     */
    @Select("SELECT * FROM chat_messages WHERE user_id = #{userId} AND is_favorite = 1 AND deleted = 0 ORDER BY create_time DESC")
    IPage<ChatMessage> findFavoriteMessagesByUserId(Page<ChatMessage> page, @Param("userId") Long userId);

    /**
     * 查询用户的置顶消息
     * 
     * @param userId 用户ID
     * @return 置顶消息列表
     */
    @Select("SELECT * FROM chat_messages WHERE user_id = #{userId} AND is_pinned = 1 AND deleted = 0 ORDER BY create_time DESC")
    List<ChatMessage> findPinnedMessagesByUserId(@Param("userId") Long userId);

    /**
     * 根据标签查询消息列表
     * 
     * @param page 分页参数
     * @param tags 标签列表
     * @return 消息分页列表
     */
    IPage<ChatMessage> findByTags(Page<ChatMessage> page, @Param("tags") List<String> tags);

    /**
     * 根据相关菜谱ID查询消息列表
     * 
     * @param page 分页参数
     * @param recipeId 菜谱ID
     * @return 消息分页列表
     */
    @Select("SELECT * FROM chat_messages WHERE related_recipe_id = #{recipeId} AND deleted = 0 ORDER BY create_time DESC")
    IPage<ChatMessage> findByRelatedRecipeId(Page<ChatMessage> page, @Param("recipeId") Long recipeId);

    /**
     * 搜索消息
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param keyword 关键词
     * @return 消息分页列表
     */
    IPage<ChatMessage> searchMessages(Page<ChatMessage> page, @Param("userId") Long userId, @Param("keyword") String keyword);

    /**
     * 更新消息状态
     * 
     * @param messageId 消息ID
     * @param status 状态
     * @return 更新行数
     */
    @Update("UPDATE chat_messages SET status = #{status} WHERE id = #{messageId}")
    int updateStatus(@Param("messageId") Long messageId, @Param("status") Integer status);

    /**
     * 更新消息内容
     * 
     * @param messageId 消息ID
     * @param content 消息内容
     * @param formattedContent 格式化内容
     * @return 更新行数
     */
    @Update("UPDATE chat_messages SET content = #{content}, formatted_content = #{formattedContent}, edit_count = edit_count + 1, edit_time = NOW() WHERE id = #{messageId}")
    int updateContent(@Param("messageId") Long messageId, @Param("content") String content, @Param("formattedContent") String formattedContent);

    /**
     * 更新消息阅读状态
     * 
     * @param messageId 消息ID
     * @return 更新行数
     */
    @Update("UPDATE chat_messages SET read_time = NOW() WHERE id = #{messageId} AND read_time IS NULL")
    int markAsRead(@Param("messageId") Long messageId);

    /**
     * 批量标记消息为已读
     * 
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 更新行数
     */
    @Update("UPDATE chat_messages SET read_time = NOW() WHERE session_id = #{sessionId} AND user_id = #{userId} AND read_time IS NULL")
    int batchMarkAsRead(@Param("sessionId") Long sessionId, @Param("userId") Long userId);

    /**
     * 更新消息置顶状态
     * 
     * @param messageId 消息ID
     * @param isPinned 是否置顶
     * @return 更新行数
     */
    @Update("UPDATE chat_messages SET is_pinned = #{isPinned} WHERE id = #{messageId}")
    int updatePinnedStatus(@Param("messageId") Long messageId, @Param("isPinned") Boolean isPinned);

    /**
     * 更新消息收藏状态
     * 
     * @param messageId 消息ID
     * @param isFavorite 是否收藏
     * @return 更新行数
     */
    @Update("UPDATE chat_messages SET is_favorite = #{isFavorite} WHERE id = #{messageId}")
    int updateFavoriteStatus(@Param("messageId") Long messageId, @Param("isFavorite") Boolean isFavorite);

    /**
     * 更新消息评分
     * 
     * @param messageId 消息ID
     * @param rating 评分
     * @return 更新行数
     */
    @Update("UPDATE chat_messages SET rating = #{rating} WHERE id = #{messageId}")
    int updateRating(@Param("messageId") Long messageId, @Param("rating") Integer rating);

    /**
     * 更新消息重试次数
     * 
     * @param messageId 消息ID
     * @return 更新行数
     */
    @Update("UPDATE chat_messages SET retry_count = retry_count + 1 WHERE id = #{messageId}")
    int incrementRetryCount(@Param("messageId") Long messageId);

    /**
     * 撤回消息
     * 
     * @param messageId 消息ID
     * @return 更新行数
     */
    @Update("UPDATE chat_messages SET status = 4, recall_time = NOW() WHERE id = #{messageId}")
    int recallMessage(@Param("messageId") Long messageId);

    /**
     * 统计会话消息总数
     * 
     * @param sessionId 会话ID
     * @return 消息总数
     */
    @Select("SELECT COUNT(*) FROM chat_messages WHERE session_id = #{sessionId} AND deleted = 0")
    Long countMessagesBySessionId(@Param("sessionId") Long sessionId);

    /**
     * 统计用户消息总数
     * 
     * @param userId 用户ID
     * @return 消息总数
     */
    @Select("SELECT COUNT(*) FROM chat_messages WHERE user_id = #{userId} AND deleted = 0")
    Long countMessagesByUserId(@Param("userId") Long userId);

    /**
     * 统计消息总数
     * 
     * @return 消息总数
     */
    @Select("SELECT COUNT(*) FROM chat_messages WHERE deleted = 0")
    Long countMessages();

    /**
     * 统计未读消息数
     * 
     * @param userId 用户ID
     * @return 未读消息数
     */
    @Select("SELECT COUNT(*) FROM chat_messages WHERE user_id = #{userId} AND sender_type = 2 AND read_time IS NULL AND deleted = 0")
    Long countUnreadMessagesByUserId(@Param("userId") Long userId);

    /**
     * 根据消息类型统计消息分布
     * 
     * @return 消息类型分布
     */
    @Select("SELECT " +
            "CASE message_type " +
            "WHEN 1 THEN '文本' " +
            "WHEN 2 THEN '图片' " +
            "WHEN 3 THEN '文件' " +
            "WHEN 4 THEN '语音' " +
            "WHEN 5 THEN '视频' " +
            "WHEN 6 THEN '菜谱卡片' " +
            "WHEN 7 THEN '系统消息' " +
            "ELSE '其他' " +
            "END as message_type_name, " +
            "COUNT(*) as count " +
            "FROM chat_messages WHERE deleted = 0 " +
            "GROUP BY message_type ORDER BY count DESC")
    List<Object> countMessagesByType();

    /**
     * 根据发送者类型统计消息分布
     * 
     * @return 发送者类型分布
     */
    @Select("SELECT " +
            "CASE sender_type " +
            "WHEN 1 THEN '用户' " +
            "WHEN 2 THEN 'AI助手' " +
            "WHEN 3 THEN '系统' " +
            "ELSE '其他' " +
            "END as sender_type_name, " +
            "COUNT(*) as count " +
            "FROM chat_messages WHERE deleted = 0 " +
            "GROUP BY sender_type ORDER BY sender_type")
    List<Object> countMessagesBySenderType();

    /**
     * 统计Token使用情况
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return Token使用统计
     */
    @Select("SELECT " +
            "SUM(input_tokens) as total_input_tokens, " +
            "SUM(output_tokens) as total_output_tokens, " +
            "SUM(total_tokens) as total_tokens, " +
            "SUM(cost) as total_cost, " +
            "COUNT(*) as message_count " +
            "FROM chat_messages " +
            "WHERE create_time BETWEEN #{startTime} AND #{endTime} " +
            "AND deleted = 0")
    Object getTokenUsageStats(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 获取用户最近的消息
     * 
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 最近消息列表
     */
    @Select("SELECT * FROM chat_messages WHERE user_id = #{userId} AND deleted = 0 ORDER BY create_time DESC LIMIT #{limit}")
    List<ChatMessage> getRecentMessagesByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);

    /**
     * 获取高评分消息
     * 
     * @param limit 限制数量
     * @param minRating 最小评分
     * @return 高评分消息列表
     */
    @Select("SELECT * FROM chat_messages WHERE rating >= #{minRating} AND deleted = 0 ORDER BY rating DESC, create_time DESC LIMIT #{limit}")
    List<ChatMessage> getHighRatedMessages(@Param("limit") Integer limit, @Param("minRating") Integer minRating);

    /**
     * 获取错误消息列表
     * 
     * @param page 分页参数
     * @return 错误消息分页列表
     */
    @Select("SELECT * FROM chat_messages WHERE status = 3 AND error_message IS NOT NULL AND deleted = 0 ORDER BY create_time DESC")
    IPage<ChatMessage> getErrorMessages(Page<ChatMessage> page);

    /**
     * 清理过期消息数据
     * 
     * @param beforeTime 清理时间点
     * @return 清理行数
     */
    @Update("UPDATE chat_messages SET deleted = 1 WHERE create_time < #{beforeTime} AND status IN (3, 4)")
    int cleanupOldMessages(@Param("beforeTime") LocalDateTime beforeTime);

    /**
     * 获取会话中的下一个序号
     * 
     * @param sessionId 会话ID
     * @return 下一个序号
     */
    @Select("SELECT COALESCE(MAX(sequence_number), 0) + 1 FROM chat_messages WHERE session_id = #{sessionId}")
    Integer getNextSequenceNumber(@Param("sessionId") Long sessionId);
}