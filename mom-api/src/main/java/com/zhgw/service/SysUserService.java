package com.zhgw.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhgw.entity.SysUser;
import com.zhgw.model.dto.ProfileUpdateDTO;
import com.zhgw.model.dto.UserDTO;
import com.zhgw.model.vo.UserInfoVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface SysUserService extends IService<SysUser> {
    
    /**
     * 根据用户名查询用户
     */
    SysUser findByUsername(String username);
    
    /**
     * 检查用户名是否存在
     */
    boolean checkUsernameExists(String username);
    
    /**
     * 检查手机号是否存在（排除指定用户）
     */
    boolean checkPhoneExists(String phone, Long excludeUserId);
    
    /**
     * 检查邮箱是否存在（排除指定用户）
     */
    boolean checkEmailExists(String email, Long excludeUserId);
    
    /**
     * 导出用户数据
     */
    void exportUser(List<SysUser> userList, HttpServletResponse response);
    
    /**
     * 导入用户数据
     */
    void importUser(MultipartFile file, boolean updateSupport) throws Exception;
    
    /**
     * 导出模板
     */
    void exportTemplate(HttpServletResponse response);
    
    /**
     * 获取当前用户信息
     */
    UserInfoVO getCurrentUserInfo();
    
    /**
     * 更新用户个人信息
     */
    void updateProfile(ProfileUpdateDTO profileDTO);
    
    /**
     * 上传用户头像
     */
    String uploadAvatar(MultipartFile file) throws IOException;
    
    /**
     * 修改用户密码
     */
    void updatePassword(String oldPassword, String newPassword);
    
    /**
     * 获取用户分页列表
     */
    Page<UserInfoVO> getUserPage(Integer pageNum, Integer pageSize, UserDTO userDTO);
} 