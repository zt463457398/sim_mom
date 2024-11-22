package com.zhgw.common.exception;

import com.zhgw.common.api.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenException.class)
    public R<String> handleTokenException(TokenException e) {
        log.error("Token异常：", e);
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<String> handleException(Exception e) {
        log.error("系统异常：", e);
        return R.fail("系统异常，请稍后重试");
    }
} 