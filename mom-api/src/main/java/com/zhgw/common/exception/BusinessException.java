package com.zhgw.common.exception;

/**
 * 业务异常基类
 */
public class BusinessException extends RuntimeException {
    private int code;
    private String message;

    public BusinessException(String message) {
        super(message);
        this.message = message;
        this.code = 500;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
} 