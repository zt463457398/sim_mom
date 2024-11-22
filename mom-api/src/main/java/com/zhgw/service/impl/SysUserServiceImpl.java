package com.zhgw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhgw.mapper.SysUserMapper;
import com.zhgw.model.entity.SysUser;
import com.zhgw.model.param.LoginParam;
import com.zhgw.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {
    
    private final SysUserMapper sysUserMapper;

    @Override
    public SysUser login(LoginParam loginParam) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, loginParam.getUsername())
                .eq(SysUser::getPassword, loginParam.getPassword())
                .eq(SysUser::getStatus, 1);
        return sysUserMapper.selectOne(wrapper);
    }
} 