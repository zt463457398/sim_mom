package com.zhgw.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 日志工具类
 * 用于管理日志上下文信息和脱敏处理
 */
public class LogUtils {
    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    public static final String TRACE_ID = "traceId";
    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";
    public static final String REQUEST_URI = "requestUri";
    public static final String REQUEST_METHOD = "requestMethod";
    public static final String CLIENT_IP = "clientIp";

    // 敏感数据正则表达式
    private static final Pattern PHONE_PATTERN = Pattern.compile("1[3-9]\\d{9}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("\\d{17}[0-9Xx]");
    private static final Pattern BANK_CARD_PATTERN = Pattern.compile("\\d{16,19}");

    /**
     * 初始化MDC上下文
     */
    public static void initMDC() {
        MDC.put(TRACE_ID, generateTraceId());
    }

    /**
     * 清理MDC上下文
     */
    public static void clearMDC() {
        MDC.clear();
    }

    /**
     * 添加用户信息到MDC（带脱敏）
     */
    public static void setUserInfo(String userId, String username) {
        if (userId != null) {
            MDC.put(USER_ID, userId);
        }
        if (username != null) {
            MDC.put(USERNAME, SensitiveDataUtils.maskName(username));
        }
    }

    /**
     * 添加请求信息到MDC
     */
    public static void setRequestInfo(String uri, String method, String clientIp) {
        MDC.put(REQUEST_URI, uri);
        MDC.put(REQUEST_METHOD, method);
        MDC.put(CLIENT_IP, clientIp);
    }

    /**
     * 生成追踪ID
     */
    private static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 对日志消息进行脱敏处理
     */
    public static String maskSensitiveInfo(String message) {
        if (message == null) {
            return null;
        }

        // 手机号脱敏
        message = PHONE_PATTERN.matcher(message).replaceAll(matchResult -> 
            SensitiveDataUtils.maskPhone(matchResult.group()));

        // 邮箱脱敏
        message = EMAIL_PATTERN.matcher(message).replaceAll(matchResult -> 
            SensitiveDataUtils.maskEmail(matchResult.group()));

        // 身份证号脱敏
        message = ID_CARD_PATTERN.matcher(message).replaceAll(matchResult -> 
            SensitiveDataUtils.maskIdCard(matchResult.group()));

        // 银行卡号脱敏
        message = BANK_CARD_PATTERN.matcher(message).replaceAll(matchResult -> 
            SensitiveDataUtils.maskBankCard(matchResult.group()));

        // 密码脱敏（替换password=xxx的形式）
        message = message.replaceAll("password=([^,}\\s]+)", "password=" + SensitiveDataUtils.maskPassword(""));

        return message;
    }

    /**
     * 获取日志记录器
     */
    public static Logger getLogger() {
        return logger;
    }
} 