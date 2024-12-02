package com.zhgw.common.monitor;

import com.zhgw.common.enums.ErrorCodeEnum;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 错误码监控工具
 * 用于统计错误码的使用频率和分布
 */
@Component
public class ErrorCodeMonitor {

    private final MeterRegistry meterRegistry;
    private final Map<ErrorCodeEnum, Counter> errorCounters;
    private final Map<String, Counter> moduleCounters;
    private final Map<Integer, Long> lastMinuteErrors;
    private static final String METRIC_PREFIX = "error_code";

    public ErrorCodeMonitor(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.errorCounters = new EnumMap<>(ErrorCodeEnum.class);
        this.moduleCounters = new ConcurrentHashMap<>();
        this.lastMinuteErrors = new ConcurrentHashMap<>();
        initializeCounters();
    }

    private void initializeCounters() {
        // 初始化每个错误码的计数器
        for (ErrorCodeEnum errorCode : ErrorCodeEnum.values()) {
            Counter counter = Counter.builder(METRIC_PREFIX)
                    .tag("code", String.valueOf(errorCode.getCode()))
                    .tag("message", errorCode.getMessage())
                    .tag("module", getModuleName(errorCode))
                    .description("Error code occurrence counter")
                    .register(meterRegistry);
            errorCounters.put(errorCode, counter);
        }

        // 初始化每个模块的计数器
        for (int i = 0; i <= 7; i++) {
            String moduleName = getModuleName(i);
            Counter counter = Counter.builder(METRIC_PREFIX + "_module")
                    .tag("module", moduleName)
                    .description("Module error counter")
                    .register(meterRegistry);
            moduleCounters.put(moduleName, counter);
        }
    }

    /**
     * 记录错误码出现
     */
    public void recordError(ErrorCodeEnum errorCode) {
        // 增加错误码计数
        errorCounters.get(errorCode).increment();
        
        // 增加模块计数
        String moduleName = getModuleName(errorCode);
        moduleCounters.get(moduleName).increment();
        
        // 记录最近一分钟的错误
        lastMinuteErrors.put(errorCode.getCode(), System.currentTimeMillis());
        
        // 清理超过1分钟的记录
        cleanupOldRecords();
    }

    /**
     * 获取最近一分钟的错误次数
     */
    public int getLastMinuteErrorCount() {
        cleanupOldRecords();
        return lastMinuteErrors.size();
    }

    /**
     * 获取指定模块的错误次数
     */
    public double getModuleErrorCount(String moduleName) {
        Counter counter = moduleCounters.get(moduleName);
        return counter != null ? counter.count() : 0;
    }

    /**
     * 获取指定错误码的出现次数
     */
    public double getErrorCodeCount(ErrorCodeEnum errorCode) {
        Counter counter = errorCounters.get(errorCode);
        return counter != null ? counter.count() : 0;
    }

    private void cleanupOldRecords() {
        long currentTime = System.currentTimeMillis();
        lastMinuteErrors.entrySet().removeIf(entry -> 
            currentTime - entry.getValue() > 60000); // 60秒
    }

    private String getModuleName(ErrorCodeEnum errorCode) {
        return getModuleName((errorCode.getCode() / 100) % 100);
    }

    private String getModuleName(int moduleCode) {
        return switch (moduleCode) {
            case 0 -> "系统通用模块";
            case 1 -> "用户模块";
            case 2 -> "认证模块";
            case 3 -> "权限模块";
            case 4 -> "文件模块";
            case 5 -> "订单模块";
            case 6 -> "产品模块";
            case 7 -> "操作类模块";
            default -> "其他模块";
        };
    }
} 