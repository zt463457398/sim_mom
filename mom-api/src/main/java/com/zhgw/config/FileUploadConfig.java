package com.zhgw.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "upload")
public class FileUploadConfig {
    /**
     * 文件上传根路径
     */
    private String path;
    
    /**
     * 文件大小限制（字节）
     */
    private Long maxSize;
    
    /**
     * 允许的文件类型（MIME类型）
     */
    private List<String> allowedTypes;
    
    /**
     * 文件访问URL前缀
     */
    private String accessUrl;

    /**
     * 获取格式化后的文件路径（确保以/结尾）
     */
    public String getFormattedPath() {
        return path.endsWith("/") ? path : path + "/";
    }
} 