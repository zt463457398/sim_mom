package com.zhgw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhgw.entity.SysDept;
import com.zhgw.model.vo.DeptTreeVO;

import java.util.List;

public interface SysDeptService extends IService<SysDept> {
    
    // 获取部门树形结构
    List<DeptTreeVO> getDeptTree();
    
    // 获取部门列表
    List<SysDept> getDeptList(SysDept dept);
    
    // 获取子部门
    List<SysDept> getChildren(Long deptId);
    
    // 检查部门是否存在用户
    boolean checkDeptHasUser(Long deptId);
    
    // 检查是否有子部门
    boolean hasChildren(Long deptId);
    
    // 检查父部门是否存在
    boolean checkParentDeptExists(Long parentId);
    
    // 检查部门循环引用
    boolean checkDeptCircularRef(Long deptId, Long parentId);
    
    // 获取部门选择树
    List<DeptTreeVO> getDeptSelectTree();
} 