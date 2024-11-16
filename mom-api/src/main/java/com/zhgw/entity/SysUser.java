package com.zhgw.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    // 用户名
    private String username;
    
    // 密码
    private String password;
    
    // 真实姓名
    private String realName;
    
    // 手机号
    private String phone;
    
    // 邮箱
    private String email;
    
    // 头像
    private String avatar;
    
    // 状态（0-禁用，1-正常）
    private Integer status;
    
    // 删除标记（0-正常，1-删除）
    @TableLogic
    private Integer delFlag;
    
    // 角色ID
    private Long roleId;
    
    // 部门ID
    private Long deptId;
    
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    // 更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    // 创建者
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    // 更新���
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    
    // 备注
    private String remark;
} 