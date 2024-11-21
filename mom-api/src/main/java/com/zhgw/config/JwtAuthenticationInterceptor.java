package com.zhgw.config;

import com.zhgw.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // 放行登录接口
        if (request.getRequestURI().equals("/api/auth/login")) {
            return true;
        }

        // 获取token
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            try {
                // URL解码
                authHeader = URLDecoder.decode(authHeader, StandardCharsets.UTF_8.name());
                if (authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    // 验证token
                    if (jwtUtil.validateToken(token)) {
                        return true;
                    }
                }
            } catch (UnsupportedEncodingException e) {
                // 处理解码异常
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
        }

        // token无效，返回401
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }
} 