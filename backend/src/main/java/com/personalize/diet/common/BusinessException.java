package com.personalize.diet.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常类
 * 
 * @author PersonalizeDiet Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 详细错误信息
     */
    private Object data;

    /**
     * 构造函数
     */
    public BusinessException() {
        super();
    }

    /**
     * 构造函数
     * 
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.BUSINESS_ERROR.getCode();
        this.message = message;
    }

    /**
     * 构造函数
     * 
     * @param code 错误码
     * @param message 错误消息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 构造函数
     * 
     * @param resultCode 结果码枚举
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    /**
     * 构造函数
     * 
     * @param resultCode 结果码枚举
     * @param message 自定义错误消息
     */
    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
        this.message = message;
    }

    /**
     * 构造函数
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param data 详细错误信息
     */
    public BusinessException(Integer code, String message, Object data) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 构造函数
     * 
     * @param resultCode 结果码枚举
     * @param data 详细错误信息
     */
    public BusinessException(ResultCode resultCode, Object data) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    /**
     * 构造函数
     * 
     * @param message 错误消息
     * @param cause 原因异常
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = ResultCode.BUSINESS_ERROR.getCode();
        this.message = message;
    }

    /**
     * 构造函数
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param cause 原因异常
     */
    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    /**
     * 构造函数
     * 
     * @param resultCode 结果码枚举
     * @param cause 原因异常
     */
    public BusinessException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    // 静态工厂方法

    /**
     * 创建业务异常
     * 
     * @param message 错误消息
     * @return 业务异常
     */
    public static BusinessException of(String message) {
        return new BusinessException(message);
    }

    /**
     * 创建业务异常
     * 
     * @param code 错误码
     * @param message 错误消息
     * @return 业务异常
     */
    public static BusinessException of(Integer code, String message) {
        return new BusinessException(code, message);
    }

    /**
     * 创建业务异常
     * 
     * @param resultCode 结果码枚举
     * @return 业务异常
     */
    public static BusinessException of(ResultCode resultCode) {
        return new BusinessException(resultCode);
    }

    /**
     * 创建业务异常
     * 
     * @param resultCode 结果码枚举
     * @param message 自定义错误消息
     * @return 业务异常
     */
    public static BusinessException of(ResultCode resultCode, String message) {
        return new BusinessException(resultCode, message);
    }

    /**
     * 创建业务异常
     * 
     * @param resultCode 结果码枚举
     * @param data 详细错误信息
     * @return 业务异常
     */
    public static BusinessException of(ResultCode resultCode, Object data) {
        return new BusinessException(resultCode, data);
    }

    // 常用业务异常快捷方法

    /**
     * 参数验证失败异常
     * 
     * @param message 错误消息
     * @return 业务异常
     */
    public static BusinessException validateError(String message) {
        return new BusinessException(ResultCode.VALIDATE_ERROR, message);
    }

    /**
     * 数据不存在异常
     * 
     * @param message 错误消息
     * @return 业务异常
     */
    public static BusinessException dataNotFound(String message) {
        return new BusinessException(ResultCode.DATA_NOT_FOUND, message);
    }

    /**
     * 数据已存在异常
     * 
     * @param message 错误消息
     * @return 业务异常
     */
    public static BusinessException dataAlreadyExists(String message) {
        return new BusinessException(ResultCode.DATA_ALREADY_EXISTS, message);
    }

    /**
     * 操作不被允许异常
     * 
     * @param message 错误消息
     * @return 业务异常
     */
    public static BusinessException operationNotAllowed(String message) {
        return new BusinessException(ResultCode.OPERATION_NOT_ALLOWED, message);
    }

    /**
     * 用户不存在异常
     * 
     * @return 业务异常
     */
    public static BusinessException userNotFound() {
        return new BusinessException(ResultCode.USER_NOT_FOUND);
    }

    /**
     * 菜谱不存在异常
     * 
     * @return 业务异常
     */
    public static BusinessException recipeNotFound() {
        return new BusinessException(ResultCode.RECIPE_NOT_FOUND);
    }

    /**
     * 聊天会话不存在异常
     * 
     * @return 业务异常
     */
    public static BusinessException chatSessionNotFound() {
        return new BusinessException(ResultCode.CHAT_SESSION_NOT_FOUND);
    }

    /**
     * Dify API调用失败异常
     * 
     * @param message 错误消息
     * @return 业务异常
     */
    public static BusinessException difyApiError(String message) {
        return new BusinessException(ResultCode.DIFY_API_ERROR, message);
    }

    /**
     * 文件上传失败异常
     * 
     * @param message 错误消息
     * @return 业务异常
     */
    public static BusinessException fileUploadError(String message) {
        return new BusinessException(ResultCode.FILE_UPLOAD_ERROR, message);
    }

    /**
     * 权限不足异常
     * 
     * @return 业务异常
     */
    public static BusinessException insufficientPermissions() {
        return new BusinessException(ResultCode.INSUFFICIENT_PERMISSIONS);
    }

    /**
     * 令牌无效异常
     * 
     * @return 业务异常
     */
    public static BusinessException tokenInvalid() {
        return new BusinessException(ResultCode.TOKEN_INVALID);
    }

    /**
     * 请求过于频繁异常
     * 
     * @return 业务异常
     */
    public static BusinessException tooManyRequests() {
        return new BusinessException(ResultCode.TOO_MANY_REQUESTS);
    }
}