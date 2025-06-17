package com.personalize.diet.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举
 * 
 * @author PersonalizeDiet Team
 * @version 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    // 成功状态码
    SUCCESS(200, "操作成功"),
    CREATED(201, "创建成功"),
    ACCEPTED(202, "请求已接受"),
    NO_CONTENT(204, "无内容"),

    // 客户端错误状态码 (4xx)
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源未找到"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    NOT_ACCEPTABLE(406, "请求格式不支持"),
    CONFLICT(409, "资源冲突"),
    GONE(410, "资源已删除"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型"),
    UNPROCESSABLE_ENTITY(422, "请求参数验证失败"),
    TOO_MANY_REQUESTS(429, "请求过于频繁"),

    // 服务器错误状态码 (5xx)
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    NOT_IMPLEMENTED(501, "功能未实现"),
    BAD_GATEWAY(502, "网关错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    GATEWAY_TIMEOUT(504, "网关超时"),

    // 业务错误状态码 (1xxx)
    VALIDATE_ERROR(1001, "参数验证失败"),
    DATA_NOT_FOUND(1002, "数据不存在"),
    DATA_ALREADY_EXISTS(1003, "数据已存在"),
    DATA_INTEGRITY_VIOLATION(1004, "数据完整性约束违反"),
    OPERATION_NOT_ALLOWED(1005, "操作不被允许"),
    BUSINESS_ERROR(1006, "业务处理失败"),
    CONCURRENT_UPDATE_ERROR(1007, "并发更新冲突"),
    DATA_EXPIRED(1008, "数据已过期"),
    QUOTA_EXCEEDED(1009, "配额已超限"),
    DEPENDENCY_ERROR(1010, "依赖服务错误"),

    // 用户相关错误 (2xxx)
    USER_NOT_FOUND(2001, "用户不存在"),
    USER_ALREADY_EXISTS(2002, "用户已存在"),
    USER_DISABLED(2003, "用户已被禁用"),
    USER_LOCKED(2004, "用户已被锁定"),
    PASSWORD_ERROR(2005, "密码错误"),
    PASSWORD_EXPIRED(2006, "密码已过期"),
    ACCOUNT_EXPIRED(2007, "账户已过期"),
    INSUFFICIENT_PERMISSIONS(2008, "权限不足"),
    LOGIN_REQUIRED(2009, "需要登录"),
    TOKEN_INVALID(2010, "令牌无效"),
    TOKEN_EXPIRED(2011, "令牌已过期"),
    VERIFICATION_CODE_ERROR(2012, "验证码错误"),
    VERIFICATION_CODE_EXPIRED(2013, "验证码已过期"),

    // 菜谱相关错误 (3xxx)
    RECIPE_NOT_FOUND(3001, "菜谱不存在"),
    RECIPE_ALREADY_EXISTS(3002, "菜谱已存在"),
    RECIPE_DISABLED(3003, "菜谱已下架"),
    INGREDIENT_NOT_FOUND(3004, "食材不存在"),
    COOKING_STEP_ERROR(3005, "烹饪步骤错误"),
    NUTRITION_DATA_ERROR(3006, "营养数据错误"),
    RECIPE_CATEGORY_ERROR(3007, "菜谱分类错误"),
    RECIPE_DIFFICULTY_ERROR(3008, "菜谱难度错误"),
    RECIPE_TIME_ERROR(3009, "烹饪时间错误"),
    RECIPE_SERVING_ERROR(3010, "份量设置错误"),

    // 聊天相关错误 (4xxx)
    CHAT_SESSION_NOT_FOUND(4001, "聊天会话不存在"),
    CHAT_SESSION_EXPIRED(4002, "聊天会话已过期"),
    MESSAGE_NOT_FOUND(4003, "消息不存在"),
    MESSAGE_TOO_LONG(4004, "消息内容过长"),
    CHAT_RATE_LIMIT_EXCEEDED(4005, "聊天频率超限"),
    DIFY_API_ERROR(4006, "Dify API调用失败"),
    DIFY_API_TIMEOUT(4007, "Dify API调用超时"),
    DIFY_API_QUOTA_EXCEEDED(4008, "Dify API配额超限"),
    STREAM_CONNECTION_ERROR(4009, "流式连接错误"),
    WEBSOCKET_CONNECTION_ERROR(4010, "WebSocket连接错误"),

    // 文件相关错误 (5xxx)
    FILE_NOT_FOUND(5001, "文件不存在"),
    FILE_UPLOAD_ERROR(5002, "文件上传失败"),
    FILE_DOWNLOAD_ERROR(5003, "文件下载失败"),
    FILE_SIZE_EXCEEDED(5004, "文件大小超限"),
    FILE_TYPE_NOT_SUPPORTED(5005, "文件类型不支持"),
    FILE_STORAGE_ERROR(5006, "文件存储错误"),
    IMAGE_PROCESSING_ERROR(5007, "图片处理失败"),
    FILE_PERMISSION_DENIED(5008, "文件权限不足"),

    // 缓存相关错误 (6xxx)
    CACHE_ERROR(6001, "缓存操作失败"),
    CACHE_KEY_NOT_FOUND(6002, "缓存键不存在"),
    CACHE_EXPIRED(6003, "缓存已过期"),
    CACHE_SERIALIZATION_ERROR(6004, "缓存序列化失败"),
    CACHE_CONNECTION_ERROR(6005, "缓存连接失败"),

    // 外部服务错误 (7xxx)
    EXTERNAL_SERVICE_ERROR(7001, "外部服务调用失败"),
    EXTERNAL_SERVICE_TIMEOUT(7002, "外部服务调用超时"),
    EXTERNAL_SERVICE_UNAVAILABLE(7003, "外部服务不可用"),
    API_KEY_INVALID(7004, "API密钥无效"),
    API_QUOTA_EXCEEDED(7005, "API调用配额超限"),
    THIRD_PARTY_AUTH_ERROR(7006, "第三方认证失败"),

    // 系统相关错误 (8xxx)
    SYSTEM_MAINTENANCE(8001, "系统维护中"),
    SYSTEM_OVERLOAD(8002, "系统负载过高"),
    DATABASE_ERROR(8003, "数据库操作失败"),
    DATABASE_CONNECTION_ERROR(8004, "数据库连接失败"),
    CONFIGURATION_ERROR(8005, "系统配置错误"),
    RESOURCE_EXHAUSTED(8006, "系统资源耗尽"),
    NETWORK_ERROR(8007, "网络连接错误"),
    TIMEOUT_ERROR(8008, "操作超时"),

    // 安全相关错误 (9xxx)
    SECURITY_ERROR(9001, "安全检查失败"),
    CSRF_TOKEN_ERROR(9002, "CSRF令牌错误"),
    XSS_ATTACK_DETECTED(9003, "检测到XSS攻击"),
    SQL_INJECTION_DETECTED(9004, "检测到SQL注入"),
    SUSPICIOUS_ACTIVITY(9005, "检测到可疑活动"),
    IP_BLOCKED(9006, "IP地址已被封禁"),
    ENCRYPTION_ERROR(9007, "加密操作失败"),
    SIGNATURE_VERIFICATION_ERROR(9008, "签名验证失败");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 状态消息
     */
    private final String message;

    /**
     * 根据状态码获取枚举
     * 
     * @param code 状态码
     * @return 对应的枚举，如果不存在则返回 INTERNAL_SERVER_ERROR
     */
    public static ResultCode getByCode(Integer code) {
        for (ResultCode resultCode : values()) {
            if (resultCode.getCode().equals(code)) {
                return resultCode;
            }
        }
        return INTERNAL_SERVER_ERROR;
    }

    /**
     * 判断是否为成功状态码
     * 
     * @param code 状态码
     * @return 是否成功
     */
    public static boolean isSuccess(Integer code) {
        return code != null && code >= 200 && code < 300;
    }

    /**
     * 判断是否为客户端错误状态码
     * 
     * @param code 状态码
     * @return 是否为客户端错误
     */
    public static boolean isClientError(Integer code) {
        return code != null && code >= 400 && code < 500;
    }

    /**
     * 判断是否为服务器错误状态码
     * 
     * @param code 状态码
     * @return 是否为服务器错误
     */
    public static boolean isServerError(Integer code) {
        return code != null && code >= 500 && code < 600;
    }

    /**
     * 判断是否为业务错误状态码
     * 
     * @param code 状态码
     * @return 是否为业务错误
     */
    public static boolean isBusinessError(Integer code) {
        return code != null && code >= 1000 && code < 10000;
    }
}