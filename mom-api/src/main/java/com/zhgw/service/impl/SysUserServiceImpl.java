package com.zhgw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhgw.common.util.JwtUtil;
import com.zhgw.common.util.PasswordEncoder;
import com.zhgw.common.util.PasswordValidator;
import com.zhgw.mapper.SysUserMapper;
import com.zhgw.model.entity.SysUser;
import com.zhgw.model.param.LoginParam;
import com.zhgw.model.param.PasswordUpdateParam;
import com.zhgw.model.param.ProfileUpdateParam;
import com.zhgw.model.param.ResetPasswordParam;
import com.zhgw.model.param.UserAddParam;
import com.zhgw.model.param.UserQueryParam;
import com.zhgw.model.param.UserUpdateParam;
import com.zhgw.model.vo.LoginVO;
import com.zhgw.model.vo.PageVO;
import com.zhgw.service.SysUserService;
import com.zhgw.common.exception.BusinessException;
import com.zhgw.common.exception.ValidateException;
import com.zhgw.common.enums.ErrorCodeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

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
            throw new ValidateException(ErrorCodeEnum.PARAM_ERROR, "用户名或密码不能为空");
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

    @Override
    public SysUser updateProfile(Long userId, ProfileUpdateParam param) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND.getCode(), 
                                      ErrorCodeEnum.USER_NOT_FOUND.getMessage());
        }

        // 更新用户信息
        user.setRealName(param.getRealName());
        user.setPhone(param.getPhone());
        user.setEmail(param.getEmail());
        user.setUpdateTime(LocalDateTime.now());

        sysUserMapper.updateById(user);
        
        // 返回更新后的用户信息（清除密码）
        user.setPassword(null);
        return user;
    }

    @Override
    public SysUser getUserInfo(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    @Override
    public void updatePassword(Long userId, PasswordUpdateParam param) {
        // 参数校验
        if (!param.getNewPassword().equals(param.getConfirmPassword())) {
            throw new ValidateException(ErrorCodeEnum.PARAM_ERROR, "两次输入的密码不一致");
        }

        // 查询用户
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND.getCode(), 
                                      ErrorCodeEnum.USER_NOT_FOUND.getMessage());
        }

        // 验证旧密码
        if (!PasswordEncoder.matches(param.getOldPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCodeEnum.USER_PASSWORD_ERROR.getCode(), 
                                      ErrorCodeEnum.USER_PASSWORD_ERROR.getMessage());
        }

        // 更新密码
        user.setPassword(PasswordEncoder.encode(param.getNewPassword()));
        user.setUpdateTime(LocalDateTime.now());
        sysUserMapper.updateById(user);
    }

    @Override
    public PageVO<SysUser> getUserList(UserQueryParam param) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        // 添加查询条件
        wrapper.like(StringUtils.hasText(param.getUsername()), SysUser::getUsername, param.getUsername())
                .like(StringUtils.hasText(param.getRealName()), SysUser::getRealName, param.getRealName())
                .eq(param.getStatus() != null, SysUser::getStatus, param.getStatus())
                .orderByDesc(SysUser::getCreateTime);

        // 执行分页查询
        Page<SysUser> page = sysUserMapper.selectPage(
                new Page<>(param.getPageNum(), param.getPageSize()),
                wrapper
        );

        // 清除密码信息
        page.getRecords().forEach(user -> user.setPassword(null));

        return PageVO.build(page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent());
    }

    @Override
    public void updateUserStatus(Long userId, Integer status) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());
        sysUserMapper.updateById(user);
        
        // 添加日志记录
        log.info("用户状态更新: userId={}, status={}", userId, status);
    }

    @Override
    public void deleteUser(Long userId) {
        sysUserMapper.deleteById(userId);
    }

    @Override
    public void batchDeleteUsers(List<Long> userIds) {
        sysUserMapper.deleteBatchIds(userIds);
    }

    @Override
    public void addUser(UserAddParam param) {
        // 验证密码强度
        if (!PasswordValidator.isValid(param.getPassword())) {
            throw new ValidateException(ErrorCodeEnum.USER_PASSWORD_INVALID);
        }
        
        // 检查用户名是否已存在
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, param.getUsername());
        if (sysUserMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ErrorCodeEnum.USER_EXISTS.getCode(), 
                                      ErrorCodeEnum.USER_EXISTS.getMessage());
        }

        // 创建新用户
        SysUser user = new SysUser();
        user.setUsername(param.getUsername());
        user.setPassword(encryptPassword(param.getPassword()));
        user.setRealName(param.getRealName());
        user.setPhone(param.getPhone());
        user.setEmail(param.getEmail());
        user.setStatus(param.getStatus());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        sysUserMapper.insert(user);
    }

    @Override
    public void updateUser(UserUpdateParam param) {
        // 检查用户是否存在
        SysUser existUser = sysUserMapper.selectById(param.getId());
        if (existUser == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND.getCode(), 
                                      ErrorCodeEnum.USER_NOT_FOUND.getMessage());
        }

        // 更新用户信息
        SysUser user = new SysUser();
        user.setId(param.getId());
        user.setRealName(param.getRealName());
        user.setPhone(param.getPhone());
        user.setEmail(param.getEmail());
        user.setStatus(param.getStatus());
        user.setUpdateTime(LocalDateTime.now());

        sysUserMapper.updateById(user);
    }

    @Override
    public void resetPassword(ResetPasswordParam param) {
        // 验证密码强度
        if (!PasswordValidator.isValid(param.getPassword())) {
            throw new ValidateException(ErrorCodeEnum.USER_PASSWORD_INVALID);
        }
        
        // 检查用户是否存在
        SysUser existUser = sysUserMapper.selectById(param.getUserId());
        if (existUser == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND.getCode(), 
                                      ErrorCodeEnum.USER_NOT_FOUND.getMessage());
        }

        // 更新密码
        SysUser user = new SysUser();
        user.setId(param.getUserId());
        user.setPassword(encryptPassword(param.getPassword()));
        user.setUpdateTime(LocalDateTime.now());

        sysUserMapper.updateById(user);
    }
} 