package com.zhgw.common.exception;

import com.zhgw.common.enums.ErrorCodeEnum;

/**
 * 文件操作异常类
 */
public class FileException extends BusinessException {
    public FileException(String message) {
        super(ErrorCodeEnum.FILE_UPLOAD_FAILED.getCode(), message);
    }

    public FileException(ErrorCodeEnum errorCode) {
        super(errorCode.getCode(), errorCode.getMessage());
    }

    public FileException(ErrorCodeEnum errorCode, String message) {
        super(errorCode.getCode(), message);
    }
} 