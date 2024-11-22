package com.zhgw.controller;

import com.zhgw.common.annotation.RequireToken;
import com.zhgw.common.api.R;
import com.zhgw.model.param.LoginParam;
import com.zhgw.model.vo.LoginVO;
import com.zhgw.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public R<String> getUserInfo() {
        return R.ok("获取用户信息成功");
    }
} 