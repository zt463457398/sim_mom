package com.zhgw.controller;

import com.zhgw.common.api.R;
import com.zhgw.model.entity.SysUser;
import com.zhgw.model.param.LoginParam;
import com.zhgw.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class SysUserController {
    
    private final SysUserService sysUserService;

    @PostMapping("/login")
    public R<SysUser> login(@RequestBody LoginParam loginParam) {
        SysUser user = sysUserService.login(loginParam);
        if (user != null) {
            return R.ok(user);
        }
        return R.fail("用户名或密码错误");
    }
} 