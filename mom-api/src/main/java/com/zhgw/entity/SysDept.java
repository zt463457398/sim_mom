package com.zhgw.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_dept")
public class SysDept {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    // 部门名称
    private String deptName;
    
    // 父部门ID
    private Long parentId;
    
    // 显示顺序
    private Integer orderNum;
    
    // 负责人
    private String leader;
    
    // 联系电话
    private String phone;
    
    // 邮箱
    private String email;
    
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