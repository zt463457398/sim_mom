package com.zhgw.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role_menu")
public class SysRoleMenu {
    
    // 角色ID
    private Long roleId;
    
    // 菜单ID
    private Long menuId;
} 