package com.zhgw.model.vo;

import lombok.Data;
import java.util.List;

@Data
public class DeptTreeVO {
    private Long id;
    private String deptName;
    private Long parentId;
    private Integer orderNum;
    private String leader;
    private String phone;
    private String email;
    private Integer status;
    private List<DeptTreeVO> children;
    
    // 添加树形结构所需字段
    private String label;    // 节点标签，用于前端显示
    private boolean hasChildren;  // 是否有子节点
} 