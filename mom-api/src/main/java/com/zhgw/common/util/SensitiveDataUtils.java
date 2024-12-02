package com.zhgw.common.util;

/**
 * 敏感数据脱敏工具类
 */
public class SensitiveDataUtils {
    
    /**
     * 手机号码脱敏
     * 保留前3位和后4位，中间用*代替
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 邮箱脱敏
     * 邮箱前缀仅显示第一个字符和最后一个字符，中间用*代替
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String name = parts[0];
        String domain = parts[1];
        
        String maskedName;
        if (name.length() <= 2) {
            maskedName = name;
        } else {
            maskedName = name.charAt(0) + "***" + name.charAt(name.length() - 1);
        }
        
        return maskedName + "@" + domain;
    }

    /**
     * 身份证号脱敏
     * 保留前6位和后4位，中间用*代替
     */
    public static String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return idCard;
        }
        return idCard.replaceAll("(\\d{6})\\d{8}(\\w{4})", "$1********$2");
    }

    /**
     * 银行卡号脱敏
     * 仅显示后4位，其他用*代替
     */
    public static String maskBankCard(String bankCard) {
        if (bankCard == null || bankCard.length() < 4) {
            return bankCard;
        }
        return bankCard.replaceAll("\\d+(?=\\d{4})", "*");
    }

    /**
     * 姓名脱敏
     * 仅显示第一个字符，其他用*代替
     */
    public static String maskName(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() == 1) {
            return name;
        }
        if (name.length() == 2) {
            return name.substring(0, 1) + "*";
        }
        return name.substring(0, 1) + "*".repeat(name.length() - 1);
    }

    /**
     * 密码脱敏
     * 全部替换为*
     */
    public static String maskPassword(String password) {
        if (password == null) {
            return null;
        }
        return "*".repeat(6);
    }

    /**
     * 地址脱敏
     * 保留前10个字符，其余用*代替
     */
    public static String maskAddress(String address) {
        if (address == null || address.length() <= 10) {
            return address;
        }
        return address.substring(0, 10) + "****";
    }
} 