package com.zhgw.model.param;

import lombok.Data;

@Data
public class ResetPasswordParam {
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 新密码
     */
    private String password;
} 