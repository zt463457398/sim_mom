package com.zhgw.dto;

import lombok.Data;

/**
 * 登录请求数据传输对象
 * 用于接收前端登录请求的数据
 */
@Data
public class LoginRequest {
    /** 用户名 */
    private String username;
    
    /** 密码（明文） */
    private String password;
} 