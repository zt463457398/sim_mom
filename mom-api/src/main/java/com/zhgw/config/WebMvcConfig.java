package com.zhgw.config;

import com.zhgw.common.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LogInterceptor logInterceptor;

    public WebMvcConfig(LogInterceptor logInterceptor) {
        this.logInterceptor = logInterceptor;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // 注册日志拦截器，应用到所有URL
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**");
    }
} 