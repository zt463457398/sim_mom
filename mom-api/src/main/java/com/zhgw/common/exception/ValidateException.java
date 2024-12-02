package com.zhgw.common.exception;

import com.zhgw.common.enums.ErrorCodeEnum;

/**
 * 参数验证异常
 */
public class ValidateException extends BusinessException {
    public ValidateException(String message) {
        super(ErrorCodeEnum.PARAM_ERROR, message);
    }

    public ValidateException(ErrorCodeEnum errorCode) {
        super(errorCode);
    }

    public ValidateException(ErrorCodeEnum errorCode, String message) {
        super(errorCode, message);
    }
} 