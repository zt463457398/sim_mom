package com.zhgw.dto;

import lombok.Data;

/**
 * 登录响应数据传输对象
 * 用于向前端返回登录成功后的用户信息
 */
@Data
public class LoginResponse {
    /** 用户ID */
    private Long userId;
    
    /** 用户名 */
    private String username;
    
    /** 真实姓名 */
    private String realName;
    
    /** JWT令牌 */
    private String token;
} 