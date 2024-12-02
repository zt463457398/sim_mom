package com.zhgw.common.enums;

/**
 * 错误码枚举
 * 错误码设计规范：
 * 1. 错误码长度为5位数字
 * 2. 第1位表示错误级别：1-系统级错误，2-业务级错误
 * 3. 第2-3位表示模块：
 *    - 00: 系统通用
 *    - 01: 用户模块
 *    - 02: 认证模块
 *    - 03: 权限模块
 *    - 04: 文件模块
 *    - 05: 订单模块
 *    - 06: 产品模块
 * 4. 第4-5位表示具体错误码
 */
public enum ErrorCodeEnum {
    // 系统级错误码 (10xxx)
    SYSTEM_ERROR(10000, "系统异常"),
    PARAM_ERROR(10001, "参数错误"),
    DATA_ERROR(10002, "数据异常"),
    CONCURRENT_ERROR(10003, "并发异常"),
    RPC_ERROR(10004, "远程调用异常"),
    DB_ERROR(10005, "数据库异常"),
    CACHE_ERROR(10006, "缓存异常"),
    
    // 用户模块错误码 (201xx)
    USER_NOT_FOUND(20101, "用户不存在"),
    USER_PASSWORD_ERROR(20102, "密码错误"),
    USER_DISABLED(20103, "用户已禁用"),
    USER_EXISTS(20104, "用户已存在"),
    USER_ROLE_NOT_FOUND(20105, "用户角色不存在"),
    USER_PASSWORD_INVALID(20106, "密码必须包含数字和字母，长度在8-20位之间"),
    USER_ACCOUNT_LOCKED(20107, "账号已锁定"),
    USER_LOGIN_EXPIRED(20108, "登录已过期"),
    
    // 认证模块错误码 (202xx)
    TOKEN_EXPIRED(20201, "Token已过期"),
    TOKEN_INVALID(20202, "Token无效"),
    TOKEN_MISSING(20203, "Token不存在"),
    LOGIN_FAILED(20204, "登录失败"),
    LOGOUT_FAILED(20205, "登出失败"),
    
    // 权限模块错误码 (203xx)
    PERMISSION_DENIED(20301, "权限不足"),
    ROLE_NOT_FOUND(20302, "角色不存在"),
    MENU_NOT_FOUND(20303, "菜单不存在"),
    UNAUTHORIZED(20304, "未授权"),
    FORBIDDEN(20305, "禁止访问"),
    
    // 文件模块错误码 (204xx)
    FILE_UPLOAD_FAILED(20401, "文件上传失败"),
    FILE_SIZE_EXCEED(20402, "文件大小超限"),
    FILE_TYPE_NOT_ALLOWED(20403, "文件类型不允许"),
    FILE_NOT_FOUND(20404, "文件不存在"),
    FILE_DOWNLOAD_FAILED(20405, "文件下载失败"),
    
    // 订单模块错误码 (205xx)
    ORDER_NOT_FOUND(20501, "订单不存在"),
    ORDER_STATUS_ERROR(20502, "订单状态异常"),
    ORDER_CREATE_FAILED(20503, "订单创建失败"),
    ORDER_UPDATE_FAILED(20504, "订单更新失败"),
    ORDER_DELETE_FAILED(20505, "订单删除失败"),
    ORDER_PAID(20506, "订单已支付"),
    ORDER_CANCELLED(20507, "订单已取消"),
    
    // 产品模块错误码 (206xx)
    PRODUCT_NOT_FOUND(20601, "产品不存在"),
    PRODUCT_OFF_SHELF(20602, "产品已下架"),
    PRODUCT_STOCK_LOW(20603, "产品库存不足"),
    PRODUCT_PRICE_ERROR(20604, "产品价格异常"),
    PRODUCT_CREATE_FAILED(20605, "产品创建失败"),
    PRODUCT_UPDATE_FAILED(20606, "产品更新失败"),
    
    // 操作类错误码 (207xx)
    OPERATION_FAILED(20701, "操作失败"),
    DATA_NOT_FOUND(20702, "数据不存在"),
    DATA_DUPLICATE(20703, "数据重复"),
    SAVE_FAILED(20704, "保存失败"),
    UPDATE_FAILED(20705, "更新失败"),
    DELETE_FAILED(20706, "删除失败");

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

    /**
     * 根据错误码获取枚举
     */
    public static ErrorCodeEnum getByCode(int code) {
        for (ErrorCodeEnum errorCode : values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return SYSTEM_ERROR;
    }
} 