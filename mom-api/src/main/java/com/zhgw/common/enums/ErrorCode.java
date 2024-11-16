package com.zhgw.common.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    
    // 系统级错误
    SYSTEM_ERROR(500, "系统内部错误"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "无权限访问"),
    
    // 用户相关错误
    USER_NOT_FOUND(1001, "用户不存在"),
    USERNAME_EXISTS(1002, "用户名已存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    ACCOUNT_DISABLED(1004, "账号已禁用"),
    PHONE_EXISTS(1005, "手机号已存在"),
    EMAIL_EXISTS(1006, "邮箱已存在"),
    
    // 部门相关错误
    DEPT_NOT_FOUND(2001, "部门不存在"),
    DEPT_HAS_USER(2002, "部门下存在用户"),
    DEPT_HAS_CHILDREN(2003, "部门下存在子部门"),
    PARENT_DEPT_ERROR(2004, "上级部门不能是自己"),
    
    // 角色相关错误
    ROLE_NOT_FOUND(3001, "角色不存在"),
    ROLE_HAS_USER(3002, "角色下存在用户");
    
    private final Integer code;
    private final String message;
    
    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
} 