package com.zhgw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhgw.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    
    @Select("SELECT * FROM sys_user WHERE username = #{username} AND del_flag = 0")
    SysUser selectByUsername(String username);
    
    @Select("SELECT * FROM sys_user WHERE dept_id = #{deptId} AND del_flag = 0")
    List<SysUser> selectByDeptId(Long deptId);
    
    @Select("SELECT COUNT(*) FROM sys_user WHERE dept_id = #{deptId} AND del_flag = 0")
    Integer countByDeptId(Long deptId);
} 