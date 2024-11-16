package com.zhgw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhgw.entity.SysDept;
import com.zhgw.entity.SysUser;
import com.zhgw.mapper.SysDeptMapper;
import com.zhgw.mapper.SysUserMapper;
import com.zhgw.model.vo.DeptTreeVO;
import com.zhgw.service.SysDeptService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public List<DeptTreeVO> getDeptTree() {
        // 获取所有部门
        List<SysDept> allDepts = this.list(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getDelFlag, 0)
                .orderByAsc(SysDept::getOrderNum));
        
        return buildDeptTreeVO(allDepts);
    }

    @Override
    public List<SysDept> getDeptList(SysDept dept) {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<SysDept>()
                .like(dept.getDeptName() != null, SysDept::getDeptName, dept.getDeptName())
                .eq(dept.getStatus() != null, SysDept::getStatus, dept.getStatus())
                .orderByAsc(SysDept::getOrderNum);
        
        return this.list(queryWrapper);
    }

    @Override
    public List<SysDept> getChildren(Long deptId) {
        return this.list(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getParentId, deptId)
                .eq(SysDept::getDelFlag, 0)
                .orderByAsc(SysDept::getOrderNum));
    }

    @Override
    public boolean checkDeptHasUser(Long deptId) {
        return userMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getDeptId, deptId)
                .eq(SysUser::getDelFlag, 0)) > 0;
    }

    @Override
    public boolean hasChildren(Long deptId) {
        return this.count(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getParentId, deptId)
                .eq(SysDept::getDelFlag, 0)) > 0;
    }

    @Override
    public boolean checkParentDeptExists(Long parentId) {
        if (parentId == 0L) {
            return true; // 顶级部门
        }
        return this.getById(parentId) != null;
    }

    @Override
    public boolean checkDeptCircularRef(Long deptId, Long parentId) {
        if (parentId == 0L) {
            return false;
        }
        
        // 获取所有子部门ID
        List<Long> childrenIds = getChildrenDeptIds(deptId);
        return childrenIds.contains(parentId);
    }

    @Override
    public List<DeptTreeVO> getDeptSelectTree() {
        List<SysDept> allDepts = this.list(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getDelFlag, 0)
                .eq(SysDept::getStatus, 1) // 只查询正常状态的部门
                .orderByAsc(SysDept::getOrderNum));
        
        return buildDeptTreeVO(allDepts);
    }

    private List<DeptTreeVO> buildDeptTreeVO(List<SysDept> deptList) {
        // 构建父子关系
        Map<Long, List<SysDept>> parentMap = deptList.stream()
                .collect(Collectors.groupingBy(SysDept::getParentId));
        
        // 转换顶级部门
        return deptList.stream()
                .filter(dept -> dept.getParentId() == 0L)
                .map(dept -> convertToDeptTreeVO(dept, parentMap))
                .collect(Collectors.toList());
    }

    private DeptTreeVO convertToDeptTreeVO(SysDept dept, Map<Long, List<SysDept>> parentMap) {
        DeptTreeVO vo = new DeptTreeVO();
        BeanUtils.copyProperties(dept, vo);
        vo.setLabel(dept.getDeptName()); // 设置节点标签
        
        // 处理子部门
        List<SysDept> children = parentMap.get(dept.getId());
        if (children != null && !children.isEmpty()) {
            vo.setHasChildren(true);
            vo.setChildren(children.stream()
                    .map(child -> convertToDeptTreeVO(child, parentMap))
                    .collect(Collectors.toList()));
        } else {
            vo.setHasChildren(false);
            vo.setChildren(new ArrayList<>());
        }
        
        return vo;
    }

    private List<Long> getChildrenDeptIds(Long deptId) {
        List<Long> childrenIds = new ArrayList<>();
        List<SysDept> children = getChildren(deptId);
        
        for (SysDept child : children) {
            childrenIds.add(child.getId());
            childrenIds.addAll(getChildrenDeptIds(child.getId()));
        }
        
        return childrenIds;
    }
} 