package com.zhgw.service;

import com.zhgw.model.entity.SysUser;
import com.zhgw.model.param.LoginParam;
import com.zhgw.model.param.PasswordUpdateParam;
import com.zhgw.model.param.ProfileUpdateParam;
import com.zhgw.model.param.UserAddParam;
import com.zhgw.model.param.UserQueryParam;
import com.zhgw.model.param.UserUpdateParam;
import com.zhgw.model.param.ResetPasswordParam;
import com.zhgw.model.vo.LoginVO;
import com.zhgw.model.vo.PageVO;

import java.util.List;

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

    /**
     * 更新用户个人信息
     *
     * @param userId 用户ID
     * @param param 个人信息更新参数
     * @return 更新后的用户信息
     */
    SysUser updateProfile(Long userId, ProfileUpdateParam param);

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    SysUser getUserInfo(Long userId);

    /**
     * 修改密码
     */
    void updatePassword(Long userId, PasswordUpdateParam param);

    /**
     * 获取用户列表
     *
     * @param param 用户查询参数
     * @return 用户列表
     */
    PageVO<SysUser> getUserList(UserQueryParam param);

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 用户状态
     */
    void updateUserStatus(Long userId, Integer status);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     */
    void deleteUser(Long userId);

    /**
     * 批量删除用户
     *
     * @param userIds 用户ID列表
     */
    void batchDeleteUsers(List<Long> userIds);

    /**
     * 新增用户
     */
    void addUser(UserAddParam param);

    /**
     * 更新用户信息
     */
    void updateUser(UserUpdateParam param);

    /**
     * 重置用户密码
     */
    void resetPassword(ResetPasswordParam param);
} 