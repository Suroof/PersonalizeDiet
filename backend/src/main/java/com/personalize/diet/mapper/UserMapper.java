package com.personalize.diet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.diet.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户Mapper接口
 * 
 * @author PersonalizeDiet Team
 * @version 1.0.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM users WHERE username = #{username} AND deleted = 0")
    User findByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    @Select("SELECT * FROM users WHERE email = #{email} AND deleted = 0")
    User findByEmail(@Param("email") String email);

    /**
     * 根据手机号查询用户
     * 
     * @param phone 手机号
     * @return 用户信息
     */
    @Select("SELECT * FROM users WHERE phone = #{phone} AND deleted = 0")
    User findByPhone(@Param("phone") String phone);

    /**
     * 根据第三方用户ID查询用户
     * 
     * @param thirdPartyId 第三方用户ID
     * @param source 用户来源
     * @return 用户信息
     */
    @Select("SELECT * FROM users WHERE third_party_id = #{thirdPartyId} AND source = #{source} AND deleted = 0")
    User findByThirdPartyId(@Param("thirdPartyId") String thirdPartyId, @Param("source") Integer source);

    /**
     * 更新用户最后登录信息
     * 
     * @param userId 用户ID
     * @param loginTime 登录时间
     * @param loginIp 登录IP
     * @return 更新行数
     */
    @Update("UPDATE users SET last_login_time = #{loginTime}, last_login_ip = #{loginIp}, " +
            "login_count = login_count + 1, is_first_login = 0 WHERE id = #{userId}")
    int updateLastLoginInfo(@Param("userId") Long userId, 
                           @Param("loginTime") LocalDateTime loginTime, 
                           @Param("loginIp") String loginIp);

    /**
     * 更新用户状态
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 更新行数
     */
    @Update("UPDATE users SET status = #{status} WHERE id = #{userId}")
    int updateUserStatus(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 更新用户积分
     * 
     * @param userId 用户ID
     * @param points 积分变化量
     * @return 更新行数
     */
    @Update("UPDATE users SET points = points + #{points} WHERE id = #{userId}")
    int updateUserPoints(@Param("userId") Long userId, @Param("points") Integer points);

    /**
     * 根据用户标签查询用户列表
     * 
     * @param page 分页参数
     * @param tags 标签列表
     * @return 用户分页列表
     */
    IPage<User> findByTags(Page<User> page, @Param("tags") List<String> tags);

    /**
     * 根据健康目标查询用户列表
     * 
     * @param page 分页参数
     * @param healthGoal 健康目标
     * @return 用户分页列表
     */
    @Select("SELECT * FROM users WHERE health_goal = #{healthGoal} AND deleted = 0 ORDER BY create_time DESC")
    IPage<User> findByHealthGoal(Page<User> page, @Param("healthGoal") Integer healthGoal);

    /**
     * 根据活动水平查询用户列表
     * 
     * @param page 分页参数
     * @param activityLevel 活动水平
     * @return 用户分页列表
     */
    @Select("SELECT * FROM users WHERE activity_level = #{activityLevel} AND deleted = 0 ORDER BY create_time DESC")
    IPage<User> findByActivityLevel(Page<User> page, @Param("activityLevel") Integer activityLevel);

    /**
     * 根据烹饪技能查询用户列表
     * 
     * @param page 分页参数
     * @param cookingSkill 烹饪技能
     * @return 用户分页列表
     */
    @Select("SELECT * FROM users WHERE cooking_skill = #{cookingSkill} AND deleted = 0 ORDER BY create_time DESC")
    IPage<User> findByCookingSkill(Page<User> page, @Param("cookingSkill") Integer cookingSkill);

    /**
     * 查询活跃用户列表
     * 
     * @param page 分页参数
     * @param days 天数
     * @return 用户分页列表
     */
    @Select("SELECT * FROM users WHERE last_login_time >= DATE_SUB(NOW(), INTERVAL #{days} DAY) " +
            "AND deleted = 0 ORDER BY last_login_time DESC")
    IPage<User> findActiveUsers(Page<User> page, @Param("days") Integer days);

    /**
     * 查询新注册用户列表
     * 
     * @param page 分页参数
     * @param days 天数
     * @return 用户分页列表
     */
    @Select("SELECT * FROM users WHERE create_time >= DATE_SUB(NOW(), INTERVAL #{days} DAY) " +
            "AND deleted = 0 ORDER BY create_time DESC")
    IPage<User> findNewUsers(Page<User> page, @Param("days") Integer days);

    /**
     * 查询VIP用户列表
     * 
     * @param page 分页参数
     * @return 用户分页列表
     */
    @Select("SELECT * FROM users WHERE vip_expire_time > NOW() AND deleted = 0 ORDER BY vip_expire_time DESC")
    IPage<User> findVipUsers(Page<User> page);

    /**
     * 统计用户总数
     * 
     * @return 用户总数
     */
    @Select("SELECT COUNT(*) FROM users WHERE deleted = 0")
    Long countUsers();

    /**
     * 统计活跃用户数
     * 
     * @param days 天数
     * @return 活跃用户数
     */
    @Select("SELECT COUNT(*) FROM users WHERE last_login_time >= DATE_SUB(NOW(), INTERVAL #{days} DAY) AND deleted = 0")
    Long countActiveUsers(@Param("days") Integer days);

    /**
     * 统计新注册用户数
     * 
     * @param days 天数
     * @return 新注册用户数
     */
    @Select("SELECT COUNT(*) FROM users WHERE create_time >= DATE_SUB(NOW(), INTERVAL #{days} DAY) AND deleted = 0")
    Long countNewUsers(@Param("days") Integer days);

    /**
     * 根据地区统计用户分布
     * 
     * @return 地区用户分布
     */
    @Select("SELECT region, COUNT(*) as count FROM users WHERE region IS NOT NULL AND deleted = 0 GROUP BY region ORDER BY count DESC")
    List<Object> countUsersByRegion();

    /**
     * 根据年龄段统计用户分布
     * 
     * @return 年龄段用户分布
     */
    @Select("SELECT " +
            "CASE " +
            "WHEN TIMESTAMPDIFF(YEAR, birthday, CURDATE()) < 18 THEN '未成年' " +
            "WHEN TIMESTAMPDIFF(YEAR, birthday, CURDATE()) BETWEEN 18 AND 25 THEN '18-25岁' " +
            "WHEN TIMESTAMPDIFF(YEAR, birthday, CURDATE()) BETWEEN 26 AND 35 THEN '26-35岁' " +
            "WHEN TIMESTAMPDIFF(YEAR, birthday, CURDATE()) BETWEEN 36 AND 45 THEN '36-45岁' " +
            "WHEN TIMESTAMPDIFF(YEAR, birthday, CURDATE()) BETWEEN 46 AND 55 THEN '46-55岁' " +
            "ELSE '55岁以上' " +
            "END as age_group, " +
            "COUNT(*) as count " +
            "FROM users WHERE birthday IS NOT NULL AND deleted = 0 " +
            "GROUP BY age_group ORDER BY count DESC")
    List<Object> countUsersByAgeGroup();
}