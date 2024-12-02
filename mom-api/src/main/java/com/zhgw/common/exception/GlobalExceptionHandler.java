package com.zhgw.common.exception;

import com.zhgw.common.api.R;
import com.zhgw.common.enums.ErrorCodeEnum;
import com.zhgw.common.monitor.ErrorCodeMonitor;
import com.zhgw.common.util.LogUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DuplicateKeyException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ErrorCodeMonitor errorCodeMonitor;

    public GlobalExceptionHandler(ErrorCodeMonitor errorCodeMonitor) {
        this.errorCodeMonitor = errorCodeMonitor;
    }

    @ExceptionHandler(TokenException.class)
    public R<Void> handleTokenException(TokenException e, HttpServletRequest request) {
        logException("Token异常", e, request);
        errorCodeMonitor.recordError(e.getErrorCode());
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(ValidateException.class)
    public R<Void> handleValidateException(ValidateException e, HttpServletRequest request) {
        logException("参数验证异常", e, request);
        errorCodeMonitor.recordError(e.getErrorCode());
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(FileException.class)
    public R<Void> handleFileException(FileException e, HttpServletRequest request) {
        logException("文件操作异常", e, request);
        errorCodeMonitor.recordError(e.getErrorCode());
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        logException("业务异常", e, request);
        if (e.getErrorCode() != null) {
            errorCodeMonitor.recordError(e.getErrorCode());
        }
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        BusinessException businessException = new BusinessException(ErrorCodeEnum.PARAM_ERROR)
            .addContext("fields", fieldErrors.stream()
                .map(error -> error.getField() + "=" + error.getRejectedValue())
                .collect(Collectors.joining(", ")));
        
        logException("参数绑定异常", businessException, request);
        errorCodeMonitor.recordError(ErrorCodeEnum.PARAM_ERROR);
        return R.error(ErrorCodeEnum.PARAM_ERROR.getCode(), errorMessage);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public R<Void> handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        String message = e.getCause().getMessage();
        String friendlyMessage = "数据已存在";
        
        if (message.contains("uk_username")) {
            friendlyMessage = "用户名已存在";
        }
        
        BusinessException businessException = new BusinessException(ErrorCodeEnum.DATA_DUPLICATE, friendlyMessage)
            .addContext("originalMessage", message);
        
        logException("唯一约束异常", businessException, request);
        errorCodeMonitor.recordError(ErrorCodeEnum.DATA_DUPLICATE);
        return R.error(ErrorCodeEnum.DATA_DUPLICATE.getCode(), friendlyMessage);
    }

    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e, HttpServletRequest request) {
        BusinessException businessException = new BusinessException(ErrorCodeEnum.SYSTEM_ERROR)
            .addContext("exceptionClass", e.getClass().getName());
        
        logException("系统异常", businessException, request);
        errorCodeMonitor.recordError(ErrorCodeEnum.SYSTEM_ERROR);
        return R.error(ErrorCodeEnum.SYSTEM_ERROR.getCode(), "系统异常，请联系管理员");
    }

    /**
     * 统一的异常日志记录方法
     */
    private void logException(String type, BusinessException e, HttpServletRequest request) {
        // 添加请求相关的上下文信息
        e.addContext("uri", MDC.get(LogUtils.REQUEST_URI))
         .addContext("method", MDC.get(LogUtils.REQUEST_METHOD))
         .addContext("clientIP", MDC.get(LogUtils.CLIENT_IP))
         .addContext("traceId", MDC.get(LogUtils.TRACE_ID))
         .addContext("userId", MDC.get(LogUtils.USER_ID))
         .addContext("username", MDC.get(LogUtils.USERNAME))
         .addContext("userAgent", request.getHeader("User-Agent"));

        logger.error("[{}] [TraceId: {}] - {}", 
            type, 
            MDC.get(LogUtils.TRACE_ID), 
            e.getFormattedMessage(), 
            e);
    }
} 