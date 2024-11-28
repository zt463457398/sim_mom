package com.zhgw.model.param;

import lombok.Data;

@Data
public class ProfileUpdateParam {
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 手机号码
     */
    private String phone;
    
    /**
     * 邮箱地址
     */
    private String email;
} 