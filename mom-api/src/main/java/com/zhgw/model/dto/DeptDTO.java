package com.zhgw.model.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Data
public class DeptDTO {
    
    private Long id;

    @NotBlank(message = "部门名称不能为空")
    private String deptName;

    @NotNull(message = "父部门ID不能为空")
    private Long parentId;

    @NotNull(message = "显示顺序不能为空")
    private Integer orderNum;

    private String leader;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱格式不正确")
    private String email;

    private String remark;
} 