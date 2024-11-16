package com.zhgw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhgw.common.R;
import com.zhgw.entity.SysUser;
import com.zhgw.model.dto.ResetPasswordDTO;
import com.zhgw.model.dto.UserDTO;
import com.zhgw.model.dto.UserStatusDTO;
import com.zhgw.model.dto.ProfileUpdateDTO;
import com.zhgw.model.vo.UserInfoVO;
import com.zhgw.service.SysUserService;
import com.zhgw.config.FileUploadConfig;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/system/user")
@Validated
public class SysUserController {

    private static final Logger log = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    private SysUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileUploadConfig fileUploadConfig;

    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:user:list')")
    public R<Page<UserInfoVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            UserDTO userDTO) {
        return R.ok(userService.getUserPage(pageNum, pageSize, userDTO));
    }

    /**
     * 新增用户
     */
    @PostMapping
    @PreAuthorize("hasAuthority('system:user:add')")
    public R<Void> add(@Validated @RequestBody UserDTO userDTO) {
        // 检查用户名是否存在
        if (userService.checkUsernameExists(userDTO.getUsername())) {
            return R.error("用户名已存在");
        }
        // 检查手机号是否存在
        if (userDTO.getPhone() != null && userService.checkPhoneExists(userDTO.getPhone(), null)) {
            return R.error("手机号已存在");
        }
        // 检查邮箱是否存在
        if (userDTO.getEmail() != null && userService.checkEmailExists(userDTO.getEmail(), null)) {
            return R.error("邮箱已存在");
        }

        SysUser user = new SysUser();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userService.save(user);
        return R.ok();
    }

    /**
     * 修改用户
     */
    @PutMapping
    @PreAuthorize("hasAuthority('system:user:edit')")
    public R<Void> edit(@Validated @RequestBody UserDTO userDTO) {
        // 检查手机号是否存在
        if (userDTO.getPhone() != null && userService.checkPhoneExists(userDTO.getPhone(), userDTO.getId())) {
            return R.error("手机号已存在");
        }
        // 检查邮箱是否存在
        if (userDTO.getEmail() != null && userService.checkEmailExists(userDTO.getEmail(), userDTO.getId())) {
            return R.error("邮箱已存在");
        }

        SysUser user = new SysUser();
        BeanUtils.copyProperties(userDTO, user);
        userService.updateById(user);
        return R.ok();
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('system:user:query')")
    public R<SysUser> getInfo(@PathVariable Long userId) {
        return R.ok(userService.getById(userId));
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('system:user:remove')")
    public R<Void> remove(@PathVariable Long userId) {
        userService.removeById(userId);
        return R.ok();
    }

    /**
     * 重置密码
     */
    @PutMapping("/resetPwd")
    @PreAuthorize("hasAuthority('system:user:resetPwd')")
    public R<Void> resetPassword(@Validated @RequestBody ResetPasswordDTO resetPasswordDTO) {
        SysUser user = userService.getById(resetPasswordDTO.getUserId());
        if (user == null) {
            return R.error("用户不存在");
        }
        
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        userService.updateById(user);
        return R.ok();
    }

    /**
     * 更新用户状态
     */
    @PutMapping("/changeStatus")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public R<Void> changeStatus(@Validated @RequestBody UserStatusDTO statusDTO) {
        SysUser user = userService.getById(statusDTO.getUserId());
        if (user == null) {
            return R.error("用户不存在");
        }
        
        user.setStatus(statusDTO.getStatus());
        userService.updateById(user);
        return R.ok();
    }

    /**
     * 批量删除用
     */
    @DeleteMapping("/batch/{userIds}")
    @PreAuthorize("hasAuthority('system:user:remove')")
    public R<Void> batchRemove(@PathVariable List<Long> userIds) {
        userService.removeByIds(userIds);
        return R.ok();
    }

    /**
     * 导出用户
     */
    @GetMapping("/export")
    @PreAuthorize("hasAuthority('system:user:export')")
    public void export(HttpServletResponse response) {
        List<SysUser> list = userService.list(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getDelFlag, 0));
        userService.exportUser(list, response);
    }

    /**
     * 导入用户数据
     */
    @PostMapping("/importData")
    @PreAuthorize("hasAuthority('system:user:import')")
    public R<Void> importData(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "false") boolean updateSupport) {
        // 文件为空校验
        if (file.isEmpty()) {
            return R.error("请选择要导入的文件");
        }
        
        // 文件大小校验
        if (file.getSize() > fileUploadConfig.getMaxSize()) {
            return R.error("文件大小不能超过" + fileUploadConfig.getMaxSize() / 1024 / 1024 + "MB");
        }
        
        // 文件类型校验
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".xlsx")) {
            return R.error("只允许导入Excel文件(xlsx)");
        }

        try {
            userService.importUser(file, updateSupport);
            return R.ok();
        } catch (Exception e) {
            log.error("导入用户数据失败", e);
            return R.error("导入用户数据失败：" + e.getMessage());
        }
    }

    /**
     * 下载用户导入模板
     */
    @GetMapping("/importTemplate")
    @PreAuthorize("hasAuthority('system:user:import')")
    public void importTemplate(HttpServletResponse response) {
        userService.exportTemplate(response);
    }

    /**
     * 获取用户详细信息
     */
    @GetMapping("/profile")
    public R<UserInfoVO> profile() {
        return R.ok(userService.getCurrentUserInfo());
    }

    /**
     * 更新个人信息
     */
    @PutMapping("/profile")
    public R<Void> updateProfile(@Validated @RequestBody ProfileUpdateDTO profileDTO) {
        userService.updateProfile(profileDTO);
        return R.ok();
    }

    /**
     * 上传头像
     */
    @PostMapping("/avatar")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public R<String> uploadAvatar(@RequestParam("avatarFile") MultipartFile file) throws IOException {
        // 文件为空校验
        if (file.isEmpty()) {
            return R.error("请选择头像文件");
        }
        
        // 文件大小校验
        if (file.getSize() > fileUploadConfig.getMaxSize()) {
            return R.error("文件大小不能超过" + fileUploadConfig.getMaxSize() / 1024 / 1024 + "MB");
        }
        
        // 文件类型校验
        String contentType = file.getContentType();
        if (contentType == null || !fileUploadConfig.getAllowedTypes().contains(contentType.toLowerCase())) {
            return R.error("只允许上传 JPG、PNG、GIF 格式的图片");
        }

        String avatar = userService.uploadAvatar(file);
        return R.ok(avatar);
    }

    /**
     * 修改用户密码
     */
    @PutMapping("/profile/password")
    public R<Void> updatePassword(
            @RequestParam @NotBlank(message = "旧密码不能为空") String oldPassword,
            @RequestParam @NotBlank(message = "新密码不能为空") String newPassword) {
        userService.updatePassword(oldPassword, newPassword);
        return R.ok();
    }
} 