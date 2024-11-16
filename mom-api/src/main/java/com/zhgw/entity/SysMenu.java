package com.zhgw.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_menu")
public class SysMenu {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    // 菜单名称
    private String menuName;
    
    // 父菜单ID
    private Long parentId;
    
    // 显示顺序
    private Integer orderNum;
    
    // 路由地址
    private String path;
    
    // 组件路径
    private String component;
    
    // 菜单类型（M目录 C菜单 F按钮）
    private String menuType;
    
    // 权限标识
    private String perms;
    
    // 菜单图标
    private String icon;
    
    // 状态（0-禁用，1-正常）
    private Integer status;
    
    // 删除标记（0-正常，1-删除）
    @TableLogic
    private Integer delFlag;
    
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
} 