package com.zhgw.controller;

import com.zhgw.common.Result;
import com.zhgw.dto.LoginRequest;
import com.zhgw.dto.LoginResponse;
import com.zhgw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 处理用户认证相关的请求
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    /** 用户服务 */
    @Autowired
    private UserService userService;

    /**
     * 用户登录接口
     * 
     * @param request 登录请求DTO
     * @return 统一响应格式的登录结果
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 