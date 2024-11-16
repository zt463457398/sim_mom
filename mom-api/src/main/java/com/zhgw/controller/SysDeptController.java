package com.zhgw.controller;

import com.zhgw.common.R;
import com.zhgw.common.enums.ErrorCode;
import com.zhgw.entity.SysDept;
import com.zhgw.model.dto.DeptDTO;
import com.zhgw.model.vo.DeptTreeVO;
import com.zhgw.service.SysDeptService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/dept")
public class SysDeptController {

    @Autowired
    private SysDeptService deptService;

    /**
     * 获取部门树形结构
     */
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('system:dept:list')")
    public R<List<DeptTreeVO>> tree() {
        return R.ok(deptService.getDeptTree());
    }

    /**
     * 获取部门列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:dept:list')")
    public R<List<SysDept>> list(SysDept dept) {
        return R.ok(deptService.getDeptList(dept));
    }

    /**
     * 获取部门详情
     */
    @GetMapping("/{deptId}")
    @PreAuthorize("hasAuthority('system:dept:query')")
    public R<SysDept> getInfo(@PathVariable Long deptId) {
        return R.ok(deptService.getById(deptId));
    }

    /**
     * 新增部门
     */
    @PostMapping
    @PreAuthorize("hasAuthority('system:dept:add')")
    public R<Void> add(@Validated @RequestBody DeptDTO deptDTO) {
        // 检查父部门是否存在
        if (!deptService.checkParentDeptExists(deptDTO.getParentId())) {
            return R.error(ErrorCode.DEPT_NOT_FOUND.getCode(), "父部门不存在");
        }
        
        SysDept dept = new SysDept();
        BeanUtils.copyProperties(deptDTO, dept);
        deptService.save(dept);
        return R.ok();
    }

    /**
     * 修改部门
     */
    @PutMapping
    @PreAuthorize("hasAuthority('system:dept:edit')")
    public R<Void> edit(@Validated @RequestBody DeptDTO deptDTO) {
        if (deptDTO.getId().equals(deptDTO.getParentId())) {
            return R.error(ErrorCode.PARENT_DEPT_ERROR.getCode(), ErrorCode.PARENT_DEPT_ERROR.getMessage());
        }
        
        // 检查是否存在循环引用
        if (deptService.checkDeptCircularRef(deptDTO.getId(), deptDTO.getParentId())) {
            return R.error("不能设置该部门的子部门作为上级部门");
        }
        
        SysDept dept = new SysDept();
        BeanUtils.copyProperties(deptDTO, dept);
        deptService.updateById(dept);
        return R.ok();
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{deptId}")
    @PreAuthorize("hasAuthority('system:dept:remove')")
    public R<Void> remove(@PathVariable Long deptId) {
        if (deptService.checkDeptHasUser(deptId)) {
            return R.error(ErrorCode.DEPT_HAS_USER.getCode(), ErrorCode.DEPT_HAS_USER.getMessage());
        }
        if (deptService.hasChildren(deptId)) {
            return R.error(ErrorCode.DEPT_HAS_CHILDREN.getCode(), ErrorCode.DEPT_HAS_CHILDREN.getMessage());
        }
        deptService.removeById(deptId);
        return R.ok();
    }

    /**
     * 获取部门选择树
     */
    @GetMapping("/select")
    public R<List<DeptTreeVO>> select() {
        return R.ok(deptService.getDeptSelectTree());
    }
} 