package com.zhgw.common.exception;

import com.zhgw.common.api.R;
import com.zhgw.common.enums.ErrorCodeEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @ExceptionHandler(TokenException.class)
    public R<Void> handleTokenException(TokenException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        logger.error("Token异常 - 请求方法: {}, URI: {}, 错误信息: {}", method, requestURI, e.getMessage(), e);
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(ValidateException.class)
    public R<Void> handleValidateException(ValidateException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        logger.error("参数验证异常 - 请求方法: {}, URI: {}, 错误信息: {}", method, requestURI, e.getMessage(), e);
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        logger.error("业务异常 - 请求方法: {}, URI: {}, 错误信息: {}", method, requestURI, e.getMessage(), e);
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        logger.error("参数绑定异常 - 请求方法: {}, URI: {}, 错误信息: {}", method, requestURI, errorMessage, e);
        return R.error(400, errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        logger.error("系统异常 - 请求方法: {}, URI: {}, 错误信息: {}", method, requestURI, e.getMessage(), e);
        return R.error(500, "系统异常，请联系管理员");
    }

    /**
     * 处理数据库唯一约束异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public R<Void> handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        // 从异常信息中提取具体的错误信息
        String message = e.getCause().getMessage();
        String friendlyMessage = "数据已存在";
        
        // 根据具体的唯一约束判断错误信息
        if (message.contains("uk_username")) {
            friendlyMessage = "用户名已存在";
        }
        // 可以添加其他唯一约束的判断...
        
        logger.error("唯一约束异常 - 请求方法: {}, URI: {}, 错误信息: {}", method, requestURI, message);
        return R.error(ErrorCodeEnum.DATA_DUPLICATE.getCode(), friendlyMessage);
    }
} 