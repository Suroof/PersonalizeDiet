package com.personalize.diet.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.diet.entity.ChatMessage;
import com.personalize.diet.entity.ChatSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 聊天服务接口
 * 
 * @author PersonalizeDiet Team
 * @version 1.0.0
 */
public interface ChatService {

    // ==================== 会话管理 ====================

    /**
     * 创建聊天会话
     * 
     * @param userId 用户ID
     * @param title 会话标题
     * @param sessionType 会话类型
     * @param clientIp 客户端IP
     * @param userAgent 用户代理
     * @return 创建的会话
     */
    ChatSession createSession(Long userId, String title, Integer sessionType, String clientIp, String userAgent);

    /**
     * 根据ID查询会话
     * 
     * @param sessionId 会话ID
     * @return 会话信息
     */
    ChatSession getSessionById(Long sessionId);

    /**
     * 根据Dify会话ID查询会话
     * 
     * @param difySessionId Dify会话ID
     * @return 会话信息
     */
    ChatSession getSessionByDifySessionId(String difySessionId);

    /**
     * 根据Dify对话ID查询会话
     * 
     * @param difyConversationId Dify对话ID
     * @return 会话信息
     */
    ChatSession getSessionByDifyConversationId(String difyConversationId);

    /**
     * 更新会话信息
     * 
     * @param session 会话信息
     * @return 更新的会话
     */
    ChatSession updateSession(ChatSession session);

    /**
     * 删除会话
     * 
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    Boolean deleteSession(Long sessionId, Long userId);

    /**
     * 根据用户ID查询会话列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 会话分页列表
     */
    IPage<ChatSession> getSessionsByUserId(Page<ChatSession> page, Long userId);

    /**
     * 根据会话类型查询会话列表
     * 
     * @param page 分页参数
     * @param sessionType 会话类型
     * @return 会话分页列表
     */
    IPage<ChatSession> getSessionsByType(Page<ChatSession> page, Integer sessionType);

    /**
     * 查询用户的活跃会话
     * 
     * @param userId 用户ID
     * @param hours 活跃时间范围（小时）
     * @return 活跃会话列表
     */
    List<ChatSession> getActiveSessionsByUserId(Long userId, Integer hours);

    /**
     * 查询用户的置顶会话
     * 
     * @param userId 用户ID
     * @return 置顶会话列表
     */
    List<ChatSession> getPinnedSessionsByUserId(Long userId);

    /**
     * 查询用户的收藏会话
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 收藏会话分页列表
     */
    IPage<ChatSession> getFavoriteSessionsByUserId(Page<ChatSession> page, Long userId);

    /**
     * 搜索会话
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param keyword 关键词
     * @return 会话分页列表
     */
    IPage<ChatSession> searchSessions(Page<ChatSession> page, Long userId, String keyword);

    /**
     * 更新会话置顶状态
     * 
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @param isPinned 是否置顶
     * @return 是否更新成功
     */
    Boolean updateSessionPinnedStatus(Long sessionId, Long userId, Boolean isPinned);

    /**
     * 更新会话收藏状态
     * 
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @param isFavorite 是否收藏
     * @return 是否更新成功
     */
    Boolean updateSessionFavoriteStatus(Long sessionId, Long userId, Boolean isFavorite);

    /**
     * 更新会话状态
     * 
     * @param sessionId 会话ID
     * @param status 状态
     * @return 是否更新成功
     */
    Boolean updateSessionStatus(Long sessionId, Integer status);

    /**
     * 评分会话
     * 
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @param rating 评分
     * @param feedback 反馈
     * @return 是否评分成功
     */
    Boolean rateSession(Long sessionId, Long userId, Integer rating, String feedback);

    // ==================== 消息管理 ====================

    /**
     * 发送消息
     * 
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @param content 消息内容
     * @param messageType 消息类型
     * @param attachments 附件
     * @param metadata 元数据
     * @return 发送的消息
     */
    ChatMessage sendMessage(Long sessionId, Long userId, String content, Integer messageType, String attachments, String metadata);

    /**
     * 发送AI回复
     * 
     * @param sessionId 会话ID
     * @param userMessageId 用户消息ID
     * @param content 回复内容
     * @param difyMessageId Dify消息ID
     * @param metadata 元数据
     * @return 发送的消息
     */
    ChatMessage sendAiReply(Long sessionId, Long userMessageId, String content, String difyMessageId, String metadata);

    /**
     * 处理流式消息
     * 
     * @param sessionId 会话ID
     * @param streamId 流式消息ID
     * @param content 消息内容
     * @param isComplete 是否完成
     * @param sequence 序列号
     * @return 处理的消息
     */
    ChatMessage handleStreamMessage(Long sessionId, String streamId, String content, Boolean isComplete, Integer sequence);

    /**
     * 根据ID查询消息
     * 
     * @param messageId 消息ID
     * @return 消息信息
     */
    ChatMessage getMessageById(Long messageId);

    /**
     * 根据Dify消息ID查询消息
     * 
     * @param difyMessageId Dify消息ID
     * @return 消息信息
     */
    ChatMessage getMessageByDifyMessageId(String difyMessageId);

    /**
     * 根据会话ID查询消息列表
     * 
     * @param page 分页参数
     * @param sessionId 会话ID
     * @return 消息分页列表
     */
    IPage<ChatMessage> getMessagesBySessionId(Page<ChatMessage> page, Long sessionId);

    /**
     * 根据用户ID查询消息列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 消息分页列表
     */
    IPage<ChatMessage> getMessagesByUserId(Page<ChatMessage> page, Long userId);

    /**
     * 查询会话中的最后一条消息
     * 
     * @param sessionId 会话ID
     * @return 最后一条消息
     */
    ChatMessage getLastMessageBySessionId(Long sessionId);

    /**
     * 查询用户的收藏消息
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 收藏消息分页列表
     */
    IPage<ChatMessage> getFavoriteMessagesByUserId(Page<ChatMessage> page, Long userId);

    /**
     * 搜索消息
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param keyword 关键词
     * @return 消息分页列表
     */
    IPage<ChatMessage> searchMessages(Page<ChatMessage> page, Long userId, String keyword);

    /**
     * 更新消息内容
     * 
     * @param messageId 消息ID
     * @param content 消息内容
     * @param formattedContent 格式化内容
     * @return 是否更新成功
     */
    Boolean updateMessageContent(Long messageId, String content, String formattedContent);

    /**
     * 标记消息为已读
     * 
     * @param messageId 消息ID
     * @return 是否标记成功
     */
    Boolean markMessageAsRead(Long messageId);

    /**
     * 批量标记消息为已读
     * 
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 是否标记成功
     */
    Boolean batchMarkMessagesAsRead(Long sessionId, Long userId);

    /**
     * 更新消息置顶状态
     * 
     * @param messageId 消息ID
     * @param userId 用户ID
     * @param isPinned 是否置顶
     * @return 是否更新成功
     */
    Boolean updateMessagePinnedStatus(Long messageId, Long userId, Boolean isPinned);

    /**
     * 更新消息收藏状态
     * 
     * @param messageId 消息ID
     * @param userId 用户ID
     * @param isFavorite 是否收藏
     * @return 是否更新成功
     */
    Boolean updateMessageFavoriteStatus(Long messageId, Long userId, Boolean isFavorite);

    /**
     * 评分消息
     * 
     * @param messageId 消息ID
     * @param userId 用户ID
     * @param rating 评分
     * @param feedback 反馈
     * @return 是否评分成功
     */
    Boolean rateMessage(Long messageId, Long userId, Integer rating, String feedback);

    /**
     * 撤回消息
     * 
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 是否撤回成功
     */
    Boolean recallMessage(Long messageId, Long userId);

    /**
     * 重试发送消息
     * 
     * @param messageId 消息ID
     * @return 重试的消息
     */
    ChatMessage retryMessage(Long messageId);

    // ==================== Dify集成 ====================

    /**
     * 调用Dify API发送消息
     * 
     * @param sessionId 会话ID
     * @param message 消息内容
     * @param userId 用户ID
     * @param conversationId 对话ID
     * @return Dify响应
     */
    Map<String, Object> callDifyApi(Long sessionId, String message, Long userId, String conversationId);

    /**
     * 处理Dify流式响应
     * 
     * @param sessionId 会话ID
     * @param streamData 流式数据
     * @return 处理结果
     */
    Boolean handleDifyStreamResponse(Long sessionId, String streamData);

    /**
     * 同步Dify会话状态
     * 
     * @param sessionId 会话ID
     * @return 是否同步成功
     */
    Boolean syncDifySessionStatus(Long sessionId);

    /**
     * 获取Dify会话历史
     * 
     * @param conversationId 对话ID
     * @return 会话历史
     */
    List<Map<String, Object>> getDifySessionHistory(String conversationId);

    // ==================== 智能分析 ====================

    /**
     * 分析用户意图
     * 
     * @param message 消息内容
     * @param userId 用户ID
     * @return 意图分析结果
     */
    Map<String, Object> analyzeUserIntent(String message, Long userId);

    /**
     * 分析消息情感
     * 
     * @param message 消息内容
     * @return 情感分析结果
     */
    Map<String, Object> analyzeSentiment(String message);

    /**
     * 提取消息中的实体
     * 
     * @param message 消息内容
     * @return 实体提取结果
     */
    List<Map<String, Object>> extractEntities(String message);

    /**
     * 生成智能回复建议
     * 
     * @param sessionId 会话ID
     * @param messageId 消息ID
     * @return 回复建议列表
     */
    List<String> generateReplySuggestions(Long sessionId, Long messageId);

    // ==================== 统计分析 ====================

    /**
     * 统计用户会话总数
     * 
     * @param userId 用户ID
     * @return 会话总数
     */
    Long countSessionsByUserId(Long userId);

    /**
     * 统计用户消息总数
     * 
     * @param userId 用户ID
     * @return 消息总数
     */
    Long countMessagesByUserId(Long userId);

    /**
     * 统计未读消息数
     * 
     * @param userId 用户ID
     * @return 未读消息数
     */
    Long countUnreadMessagesByUserId(Long userId);

    /**
     * 统计活跃会话数
     * 
     * @param hours 活跃时间范围（小时）
     * @return 活跃会话数
     */
    Long countActiveSessions(Integer hours);

    /**
     * 根据会话类型统计会话分布
     * 
     * @return 会话类型分布
     */
    List<Object> countSessionsByType();

    /**
     * 根据消息类型统计消息分布
     * 
     * @return 消息类型分布
     */
    List<Object> countMessagesByType();

    /**
     * 统计Token使用情况
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return Token使用统计
     */
    Map<String, Object> getTokenUsageStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取用户聊天统计
     * 
     * @param userId 用户ID
     * @param days 统计天数
     * @return 聊天统计
     */
    Map<String, Object> getUserChatStats(Long userId, Integer days);

    // ==================== 数据管理 ====================

    /**
     * 导出会话数据
     * 
     * @param sessionIds 会话ID列表
     * @return 会话数据
     */
    List<ChatSession> exportSessions(List<Long> sessionIds);

    /**
     * 导出消息数据
     * 
     * @param messageIds 消息ID列表
     * @return 消息数据
     */
    List<ChatMessage> exportMessages(List<Long> messageIds);

    /**
     * 清理过期会话
     * 
     * @param beforeTime 清理时间点
     * @return 清理数量
     */
    Integer cleanupExpiredSessions(LocalDateTime beforeTime);

    /**
     * 清理过期消息
     * 
     * @param beforeTime 清理时间点
     * @return 清理数量
     */
    Integer cleanupExpiredMessages(LocalDateTime beforeTime);

    /**
     * 备份聊天数据
     * 
     * @param userId 用户ID
     * @param backupPath 备份路径
     * @return 是否备份成功
     */
    Boolean backupChatData(Long userId, String backupPath);

    /**
     * 恢复聊天数据
     * 
     * @param userId 用户ID
     * @param backupPath 备份路径
     * @return 是否恢复成功
     */
    Boolean restoreChatData(Long userId, String backupPath);
}