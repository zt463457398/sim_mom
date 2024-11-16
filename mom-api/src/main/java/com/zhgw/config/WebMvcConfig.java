package com.zhgw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Autowired
    private FileUploadConfig fileUploadConfig;

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        String path = fileUploadConfig.getPath();
        // 确保路径以 / 结尾
        path = path.endsWith("/") ? path : path + "/";
        // 确保是绝对路径
        path = path.startsWith("/") ? "file:" + path : "file:./" + path;
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(path);
    }
} 