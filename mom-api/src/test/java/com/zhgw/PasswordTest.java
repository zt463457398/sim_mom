package com.zhgw;

import com.zhgw.common.util.PasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        String password = "123456";
        String encodedPassword = PasswordEncoder.encode(password);
        System.out.println("=================================");
        System.out.println("原始密码: " + password);
        System.out.println("加密后的密码: " + encodedPassword);
        
        // 验证密码
        boolean matches = PasswordEncoder.matches(password, encodedPassword);
        System.out.println("密码验证结果: " + matches);
        System.out.println("=================================");
        
        // 生成SQL语句
        System.out.println("更新SQL:");
        System.out.println("UPDATE sys_user SET password = '" + encodedPassword + "' WHERE username = 'admin';");
        System.out.println("=================================");
    }
} 