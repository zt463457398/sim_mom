package com.zhgw.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_role")
public class SysRole {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    // 角色名称
    private String roleName;
    
    // 角色编码
    private String roleCode;
    
    // 角色描述
    private String description;
    
    // 状态（0-禁用，1-正常）
    private Integer status;
    
    // 删除标记（0-正常，1-删除）
    @TableLogic
    private Integer delFlag;
    
    // 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
    private Integer dataScope;
    
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    // 更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    // 创建者
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    // 更新者
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    
    // 备注
    private String remark;
} 