package com.zhgw.common.util;

import com.zhgw.common.enums.ErrorCodeEnum;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 错误码文档生成工具
 */
@Component
public class ErrorCodeDocGenerator {

    private static final String DOC_PATH = "docs/error-codes.md";

    @PostConstruct
    public void generateDocument() {
        // 创建目录
        File docsDir = new File("docs");
        if (!docsDir.exists()) {
            docsDir.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(DOC_PATH))) {
            generateMarkdownDoc(writer);
        } catch (IOException e) {
            LogUtils.getLogger().error("生成错误码文档失败", e);
        }
    }

    private void generateMarkdownDoc(PrintWriter writer) {
        writer.println("# 错误码文档");
        writer.println("\n> 生成时间: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        writer.println("\n## 错误码设计规范");
        writer.println("1. 错误码长度为5位数字");
        writer.println("2. 第1位表示错误级别：1-系统级错误，2-业务级错误");
        writer.println("3. 第2-3位表示模块：");
        writer.println("   - 00: 系统通用");
        writer.println("   - 01: 用户模块");
        writer.println("   - 02: 认证模块");
        writer.println("   - 03: 权限模块");
        writer.println("   - 04: 文件模块");
        writer.println("   - 05: 订单模块");
        writer.println("   - 06: 产品模块");
        writer.println("4. 第4-5位表示具体错误码");

        // 按模块分组
        Map<String, List<ErrorCodeEnum>> groupedCodes = Arrays.stream(ErrorCodeEnum.values())
                .collect(Collectors.groupingBy(this::getModuleName));

        // 生成错误码表格
        groupedCodes.forEach((module, codes) -> {
            writer.println("\n## " + module);
            writer.println("\n| 错误码 | 错误信息 | 说明 |");
            writer.println("|--------|----------|------|");
            codes.forEach(code -> 
                writer.printf("| %d | %s | - |\n", code.getCode(), code.getMessage())
            );
        });
    }

    private String getModuleName(ErrorCodeEnum code) {
        int moduleCode = (code.getCode() / 100) % 100;
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