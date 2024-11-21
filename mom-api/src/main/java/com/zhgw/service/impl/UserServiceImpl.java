package com.zhgw.service.impl;

import com.zhgw.dto.LoginRequest;
import com.zhgw.dto.LoginResponse;
import com.zhgw.entity.User;
import com.zhgw.repository.UserRepository;
import com.zhgw.service.UserService;
import com.zhgw.util.JwtUtil;
import com.zhgw.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {
    
    /** 用户数据访问层 */
    @Autowired
    private UserRepository userRepository;
    
    /** 密码加密工具 */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 实现用户登录逻辑
     * 
     * @param request 登录请求
     * @return 登录响应
     * @throws RuntimeException 登录失败时抛出异常
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        // 根据用户名查找用户，不存在则抛出异常
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
                
        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 生成JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        // 构建登录响应
        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setToken(token);
        
        return response;
    }
} 