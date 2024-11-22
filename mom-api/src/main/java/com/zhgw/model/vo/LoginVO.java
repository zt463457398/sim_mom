package com.zhgw.model.vo;

import com.zhgw.model.entity.SysUser;
import lombok.Data;

@Data
public class LoginVO {
    private String token;
    private SysUser userInfo;
} 