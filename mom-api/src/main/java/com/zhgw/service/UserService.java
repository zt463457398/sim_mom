package com.zhgw.service;

import com.zhgw.dto.LoginRequest;
import com.zhgw.dto.LoginResponse;

/**
 * 用户服务接口
 * 定义用户相关的业务操作
 */
public interface UserService {
    /**
     * 用户登录
     * 
     * @param request 登录请求DTO，包含用户名和密码
     * @return 登录响应DTO，包含用户信息和token
     * @throws RuntimeException 当用户不存在或密码错误时抛出
     */
    LoginResponse login(LoginRequest request);
} 