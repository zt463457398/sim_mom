package com.zhgw.common.enums;

/**
 * 错误码枚举
 */
public enum ErrorCodeEnum {
    // 系统级错误码
    SYSTEM_ERROR(500, "系统异常"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "无权限访问"),
    
    // 用户相关错误码
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_PASSWORD_ERROR(1002, "密码错误"),
    USER_DISABLED(1003, "用户已禁用"),
    USER_EXISTS(1004, "用户已存在"),
    USER_ROLE_NOT_FOUND(1005, "用户角色不存在"),
    USER_PASSWORD_INVALID(1006, "密码必须包含数字和字母，长度在8-20位之间"),
    
    // Token相关错误码
    TOKEN_EXPIRED(2001, "Token已过期"),
    TOKEN_INVALID(2002, "Token无效"),
    TOKEN_MISSING(2003, "Token不存在"),
    
    // 权限相关错误码
    PERMISSION_DENIED(3001, "权限不足"),
    ROLE_NOT_FOUND(3002, "角色不存在"),
    MENU_NOT_FOUND(3003, "菜单不存在"),
    
    // 操作相关错误码
    OPERATION_FAILED(4001, "操作失败"),
    DATA_NOT_FOUND(4002, "数据不存在"),
    DATA_DUPLICATE(4003, "数据重复"),
    
    // 文件相关错误码
    FILE_UPLOAD_FAILED(5001, "文件上传失败"),
    FILE_SIZE_EXCEED(5002, "文件大小超限"),
    FILE_TYPE_NOT_ALLOWED(5003, "文件类型不允许");

    private final int code;
    private final String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 