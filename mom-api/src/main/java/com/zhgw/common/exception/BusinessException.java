package com.zhgw.common.exception;

import com.zhgw.common.enums.ErrorCodeEnum;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务异常基类
 * 提供更丰富的上下文信息，便于问题定位和日志记录
 */
public class BusinessException extends RuntimeException {
    private final int code;
    private final String message;
    private final LocalDateTime timestamp;
    private final Map<String, Object> context;
    private ErrorCodeEnum errorCode;

    public BusinessException(String message) {
        this(ErrorCodeEnum.SYSTEM_ERROR.getCode(), message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.context = new HashMap<>();
    }

    public BusinessException(ErrorCodeEnum errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCodeEnum errorCode, String message) {
        this(errorCode.getCode(), message);
        this.errorCode = errorCode;
    }

    /**
     * 添加上下文信息
     * @param key 上下文键
     * @param value 上下文值
     * @return this，支持链式调用
     */
    public BusinessException addContext(String key, Object value) {
        this.context.put(key, value);
        return this;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getContext() {
        return new HashMap<>(context);
    }

    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }

    /**
     * 获取格式化的错误信息，包含上下文信息
     */
    public String getFormattedMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("错误码: ").append(code)
          .append(", 消息: ").append(message)
          .append(", 时间: ").append(timestamp);
        
        if (!context.isEmpty()) {
            sb.append(", 上下文: ").append(context);
        }
        
        return sb.toString();
    }
} 