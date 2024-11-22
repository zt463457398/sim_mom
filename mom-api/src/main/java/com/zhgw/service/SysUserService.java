package com.zhgw.service;

import com.zhgw.model.entity.SysUser;
import com.zhgw.model.param.LoginParam;

public interface SysUserService {
    SysUser login(LoginParam loginParam);
} 