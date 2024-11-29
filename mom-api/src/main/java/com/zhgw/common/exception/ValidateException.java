package com.zhgw.common.exception;

/**
 * 参数验证异常
 */
public class ValidateException extends BusinessException {
    public ValidateException(String message) {
        super(400, message);
    }

    public ValidateException(int code, String message) {
        super(code, message);
    }
} 