package com.zhgw.common.handler;

import com.zhgw.common.R;
import com.zhgw.common.exception.BusinessException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e) {
        log.error(e.getMessage(), e);
        return R.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error(message, e);
        return R.error(400, message);
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getMessage();
        log.error(message, e);
        return R.error(400, message);
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException e) {
        String message = e.getAllErrors().get(0).getDefaultMessage();
        log.error(message, e);
        return R.error(400, message);
    }

    /**
     * 处理文件大小超限异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public R<Void> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("文件大小超过限制", e);
        return R.error(400, "文件大小超过限制");
    }

    /**
     * 处理文件上传异常
     */
    @ExceptionHandler(MultipartException.class)
    public R<Void> handleMultipartException(MultipartException e) {
        log.error("文件上传失败", e);
        return R.error(400, "文件上传失败");
    }

    /**
     * 处理IO异常
     */
    @ExceptionHandler(IOException.class)
    public R<Void> handleIOException(IOException e) {
        log.error("文件操作失败", e);
        return R.error(500, "文件操作失败");
    }

    /**
     * 处理其他未知异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统错误", e);
        return R.error(500, "系统错误，请联系管理员");
    }
} 