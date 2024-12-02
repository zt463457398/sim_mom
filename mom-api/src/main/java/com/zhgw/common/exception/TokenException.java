package com.zhgw.common.exception;

import com.zhgw.common.enums.ErrorCodeEnum;

/**
 * Token异常类
 */
public class TokenException extends BusinessException {
    public TokenException(String message) {
        super(ErrorCodeEnum.TOKEN_INVALID, message);
    }

    public TokenException(ErrorCodeEnum errorCode) {
        super(errorCode);
    }

    public TokenException(ErrorCodeEnum errorCode, String message) {
        super(errorCode, message);
    }
} 