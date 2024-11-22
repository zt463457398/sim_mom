package com.zhgw.common.util;

import java.util.regex.Pattern;

public class PasswordValidator {
    // 密码必须包含数字和字母，长度8-20位
    private static final Pattern PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$");

    public static boolean isValid(String password) {
        return PATTERN.matcher(password).matches();
    }

    public static String getPasswordRule() {
        return "密码必须包含数字和字母，长度在8-20位之间";
    }
} 