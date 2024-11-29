package com.zhgw.common.exception;

/**
 * Token异常类
 */
public class TokenException extends BusinessException {
    public TokenException(String message) {
        super(401, message);
    }

    public TokenException(int code, String message) {
        super(code, message);
    }
} 