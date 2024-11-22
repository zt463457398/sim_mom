package com.zhgw.common.interceptor;

import com.zhgw.common.annotation.RequireToken;
import com.zhgw.common.exception.TokenException;
import com.zhgw.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // 如果不是映射到方法，直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequireToken requireToken = handlerMethod.getMethodAnnotation(RequireToken.class);
        
        // 如果没有@RequireToken注解，直接通过
        if (requireToken == null || !requireToken.required()) {
            return true;
        }

        // 获取token
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            log.warn("无效的token格式: {}", token);
            throw new TokenException("无效的token");
        }

        // 验证token
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            log.warn("token已过期或无效: {}", token);
            throw new TokenException("token已过期或无效");
        }

        // 将用户信息存入request
        request.setAttribute("userId", jwtUtil.getUserIdFromToken(token));
        request.setAttribute("username", jwtUtil.getUsernameFromToken(token));
        
        return true;
    }
} 