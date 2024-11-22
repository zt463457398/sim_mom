package com.zhgw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhgw.common.util.JwtUtil;
import com.zhgw.common.util.PasswordEncoder;
import com.zhgw.mapper.SysUserMapper;
import com.zhgw.model.entity.SysUser;
import com.zhgw.model.param.LoginParam;
import com.zhgw.model.vo.LoginVO;
import com.zhgw.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class SysUserServiceImpl implements SysUserService {
    
    private final SysUserMapper sysUserMapper;
    private final JwtUtil jwtUtil;

    @Override
    public LoginVO login(LoginParam loginParam) {
        // 参数校验
        if (loginParam == null || !StringUtils.hasText(loginParam.getUsername()) 
            || !StringUtils.hasText(loginParam.getPassword())) {
            throw new IllegalArgumentException("用户名或密码不能为空");
        }

        // 查询用户
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, loginParam.getUsername())
                .eq(SysUser::getStatus, 1);
        SysUser user = sysUserMapper.selectOne(wrapper);
        
        // 用户不存在，返回null
        if (user == null) {
            log.warn("用户不存在: {}", loginParam.getUsername());
            return null;
        }

        // 验证密码
        if (!PasswordEncoder.matches(loginParam.getPassword(), user.getPassword())) {
            log.warn("密码错误: {}", loginParam.getUsername());
            return null;
        }

        // 生成token
        String token = jwtUtil.generateToken(user.getUsername(), user.getId());
        
        // 构建返回结果
        LoginVO loginVO = new LoginVO();
        user.setPassword(null);  // 清空密码
        loginVO.setUserInfo(user);
        loginVO.setToken(token);
        
        log.info("用户登录成功: {}", loginParam.getUsername());
        return loginVO;
    }

    @Override
    public String encryptPassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return PasswordEncoder.encode(password);
    }
} 