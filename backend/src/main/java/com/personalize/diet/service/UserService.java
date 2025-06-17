package com.personalize.diet.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.diet.entity.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户服务接口
 * 
 * @author PersonalizeDiet Team
 * @version 1.0.0
 */
public interface UserService {

    /**
     * 根据ID查询用户
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    User getById(Long id);

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);

    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    User getByEmail(String email);

    /**
     * 根据手机号查询用户
     * 
     * @param phone 手机号
     * @return 用户信息
     */
    User getByPhone(String phone);

    /**
     * 根据第三方ID查询用户
     * 
     * @param thirdPartyId 第三方ID
     * @param source 来源
     * @return 用户信息
     */
    User getByThirdPartyId(String thirdPartyId, String source);

    /**
     * 创建用户
     * 
     * @param user 用户信息
     * @return 创建的用户
     */
    User createUser(User user);

    /**
     * 更新用户信息
     * 
     * @param user 用户信息
     * @return 更新的用户
     */
    User updateUser(User user);

    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 是否删除成功
     */
    Boolean deleteUser(Long id);

    /**
     * 用户注册
     * 
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     * @param phone 手机号
     * @return 注册的用户
     */
    User register(String username, String password, String email, String phone);

    /**
     * 用户登录
     * 
     * @param loginName 登录名（用户名/邮箱/手机号）
     * @param password 密码
     * @param clientIp 客户端IP
     * @param userAgent 用户代理
     * @return 登录的用户
     */
    User login(String loginName, String password, String clientIp, String userAgent);

    /**
     * 第三方登录
     * 
     * @param thirdPartyId 第三方ID
     * @param source 来源
     * @param nickname 昵称
     * @param avatar 头像
     * @param email 邮箱
     * @return 登录的用户
     */
    User thirdPartyLogin(String thirdPartyId, String source, String nickname, String avatar, String email);

    /**
     * 用户登出
     * 
     * @param userId 用户ID
     * @return 是否登出成功
     */
    Boolean logout(Long userId);

    /**
     * 修改密码
     * 
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    Boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 重置密码
     * 
     * @param email 邮箱
     * @param newPassword 新密码
     * @param verificationCode 验证码
     * @return 是否重置成功
     */
    Boolean resetPassword(String email, String newPassword, String verificationCode);

    /**
     * 更新用户状态
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 是否更新成功
     */
    Boolean updateStatus(Long userId, Integer status);

    /**
     * 更新用户积分
     * 
     * @param userId 用户ID
     * @param points 积分变化量
     * @param reason 变化原因
     * @return 是否更新成功
     */
    Boolean updatePoints(Long userId, Integer points, String reason);

    /**
     * 更新用户等级
     * 
     * @param userId 用户ID
     * @return 是否更新成功
     */
    Boolean updateLevel(Long userId);

    /**
     * 更新用户VIP状态
     * 
     * @param userId 用户ID
     * @param isVip 是否VIP
     * @param vipExpireTime VIP过期时间
     * @return 是否更新成功
     */
    Boolean updateVipStatus(Long userId, Boolean isVip, LocalDateTime vipExpireTime);

    /**
     * 分页查询用户列表
     * 
     * @param page 分页参数
     * @return 用户分页列表
     */
    IPage<User> getUserList(Page<User> page);

    /**
     * 根据标签查询用户列表
     * 
     * @param page 分页参数
     * @param tags 标签列表
     * @return 用户分页列表
     */
    IPage<User> getUsersByTags(Page<User> page, List<String> tags);

    /**
     * 根据健康目标查询用户列表
     * 
     * @param page 分页参数
     * @param healthGoal 健康目标
     * @return 用户分页列表
     */
    IPage<User> getUsersByHealthGoal(Page<User> page, String healthGoal);

    /**
     * 根据活动水平查询用户列表
     * 
     * @param page 分页参数
     * @param activityLevel 活动水平
     * @return 用户分页列表
     */
    IPage<User> getUsersByActivityLevel(Page<User> page, String activityLevel);

    /**
     * 根据烹饪技能查询用户列表
     * 
     * @param page 分页参数
     * @param cookingSkill 烹饪技能
     * @return 用户分页列表
     */
    IPage<User> getUsersByCookingSkill(Page<User> page, String cookingSkill);

    /**
     * 查询活跃用户列表
     * 
     * @param page 分页参数
     * @param days 活跃天数
     * @return 活跃用户分页列表
     */
    IPage<User> getActiveUsers(Page<User> page, Integer days);

    /**
     * 查询新注册用户列表
     * 
     * @param page 分页参数
     * @param days 注册天数
     * @return 新注册用户分页列表
     */
    IPage<User> getNewUsers(Page<User> page, Integer days);

    /**
     * 查询VIP用户列表
     * 
     * @param page 分页参数
     * @return VIP用户分页列表
     */
    IPage<User> getVipUsers(Page<User> page);

    /**
     * 搜索用户
     * 
     * @param page 分页参数
     * @param keyword 关键词
     * @return 用户分页列表
     */
    IPage<User> searchUsers(Page<User> page, String keyword);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 是否存在
     */
    Boolean isUsernameExists(String username);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return 是否存在
     */
    Boolean isEmailExists(String email);

    /**
     * 检查手机号是否存在
     * 
     * @param phone 手机号
     * @return 是否存在
     */
    Boolean isPhoneExists(String phone);

    /**
     * 验证密码
     * 
     * @param rawPassword 原始密码
     * @param encodedPassword 加密密码
     * @return 是否匹配
     */
    Boolean verifyPassword(String rawPassword, String encodedPassword);

    /**
     * 加密密码
     * 
     * @param rawPassword 原始密码
     * @return 加密密码
     */
    String encodePassword(String rawPassword);

    /**
     * 生成用户头像URL
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @return 头像URL
     */
    String generateAvatarUrl(Long userId, String username);

    /**
     * 统计用户总数
     * 
     * @return 用户总数
     */
    Long countUsers();

    /**
     * 统计活跃用户数
     * 
     * @param days 活跃天数
     * @return 活跃用户数
     */
    Long countActiveUsers(Integer days);

    /**
     * 统计新注册用户数
     * 
     * @param days 注册天数
     * @return 新注册用户数
     */
    Long countNewUsers(Integer days);

    /**
     * 根据地区统计用户分布
     * 
     * @return 地区用户分布
     */
    List<Object> countUsersByRegion();

    /**
     * 根据年龄段统计用户分布
     * 
     * @return 年龄段用户分布
     */
    List<Object> countUsersByAgeGroup();

    /**
     * 获取用户推荐
     * 
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 推荐用户列表
     */
    List<User> getRecommendedUsers(Long userId, Integer limit);

    /**
     * 批量导入用户
     * 
     * @param users 用户列表
     * @return 导入结果
     */
    Boolean batchImportUsers(List<User> users);

    /**
     * 导出用户数据
     * 
     * @param userIds 用户ID列表
     * @return 用户数据
     */
    List<User> exportUsers(List<Long> userIds);

    /**
     * 清理无效用户数据
     * 
     * @param beforeTime 清理时间点
     * @return 清理数量
     */
    Integer cleanupInvalidUsers(LocalDateTime beforeTime);
}