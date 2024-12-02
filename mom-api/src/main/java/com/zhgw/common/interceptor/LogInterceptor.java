package com.zhgw.common.interceptor;

import com.zhgw.common.util.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 日志拦截器
 * 用于管理请求级别的日志上下文
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // 初始化MDC
        LogUtils.initMDC();
        
        // 设置请求信息
        LogUtils.setRequestInfo(
            request.getRequestURI(),
            request.getMethod(),
            request.getRemoteAddr()
        );

        // 如果有用户信息，也添加到MDC中
        Object userId = request.getAttribute("userId");
        Object username = request.getAttribute("username");
        if (userId != null && username != null) {
            LogUtils.setUserInfo(userId.toString(), username.toString());
        }

        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) {
        // 清理MDC
        LogUtils.clearMDC();
    }
} 