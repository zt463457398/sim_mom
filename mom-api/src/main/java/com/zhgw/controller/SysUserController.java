package com.zhgw.controller;

import com.zhgw.common.annotation.RequireToken;
import com.zhgw.common.api.R;
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
import com.zhgw.service.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class SysUserController {
    
    private final SysUserService sysUserService;

    @PostMapping("/login")
    public R<LoginVO> login(@RequestBody LoginParam loginParam) {
        LoginVO loginVO = sysUserService.login(loginParam);
        if (loginVO != null) {
            return R.ok(loginVO);
        }
        return R.fail("用户名或密码错误");
    }

    @GetMapping("/info")
    @RequireToken
    public R<SysUser> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(sysUserService.getUserInfo(userId));
    }

    @PutMapping("/profile")
    @RequireToken
    public R<SysUser> updateProfile(HttpServletRequest request, @RequestBody ProfileUpdateParam param) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(sysUserService.updateProfile(userId, param));
    }

    @PutMapping("/password")
    @RequireToken
    public R<Void> updatePassword(HttpServletRequest request, @RequestBody PasswordUpdateParam param) {
        Long userId = (Long) request.getAttribute("userId");
        sysUserService.updatePassword(userId, param);
        return R.ok(null);
    }

    @GetMapping("/list")
    @RequireToken
    public R<PageVO<SysUser>> getUserList(UserQueryParam param) {
        return R.ok(sysUserService.getUserList(param));
    }

    @PutMapping("/{userId}/status")
    @RequireToken
    public R<Void> updateUserStatus(@PathVariable Long userId, @RequestBody Map<String, Integer> param) {
        sysUserService.updateUserStatus(userId, param.get("status"));
        return R.ok(null);
    }

    @DeleteMapping("/{userId}")
    @RequireToken
    public R<Void> deleteUser(@PathVariable Long userId) {
        sysUserService.deleteUser(userId);
        return R.ok(null);
    }

    @DeleteMapping("/batch")
    @RequireToken
    public R<Void> batchDeleteUsers(@RequestBody List<Long> userIds) {
        sysUserService.batchDeleteUsers(userIds);
        return R.ok(null);
    }

    @PostMapping
    @RequireToken
    public R<Void> addUser(@RequestBody UserAddParam param) {
        sysUserService.addUser(param);
        return R.ok(null);
    }

    @PutMapping("/{id}")
    @RequireToken
    public R<Void> updateUser(@PathVariable Long id, @RequestBody UserUpdateParam param) {
        param.setId(id);
        sysUserService.updateUser(param);
        return R.ok(null);
    }

    @PutMapping("/{userId}/password/reset")
    @RequireToken
    public R<Void> resetPassword(@PathVariable Long userId, @RequestBody ResetPasswordParam param) {
        param.setUserId(userId);
        sysUserService.resetPassword(param);
        return R.ok(null);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public R<Void> logout(HttpServletRequest request) {
        try {
            // 获取当前用户token
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            // 可以在这里添加token失效处理逻辑
            // 比如将token加入黑名单或直接从redis中删除
            
            return R.ok(null);
        } catch (Exception e) {
            return R.fail("登出失败：" + e.getMessage());
        }
    }
} 