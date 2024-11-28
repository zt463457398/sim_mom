package com.zhgw.common.util;

public class PasswordValidator {
    /**
     * 密码规则：8-20位，必须包含数字和字母
     */
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,20}$";

    public static boolean isValid(String password) {
        return password != null && password.matches(PASSWORD_PATTERN);
    }

    public static String getPasswordRule() {
        return "密码必须包含数字和字母，长度在8-20位之间";
    }
} 