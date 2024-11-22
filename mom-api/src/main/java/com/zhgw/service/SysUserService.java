package com.zhgw.service;

import com.zhgw.model.param.LoginParam;
import com.zhgw.model.vo.LoginVO;

public interface SysUserService {
    /**
     * 用户登录
     *
     * @param loginParam 登录参数
     * @return 登录信息（包含token和用户信息）
     */
    LoginVO login(LoginParam loginParam);

    /**
     * 加密用户密码
     *
     * @param password 原始密码
     * @return 加密后的密码
     */
    String encryptPassword(String password);
} 