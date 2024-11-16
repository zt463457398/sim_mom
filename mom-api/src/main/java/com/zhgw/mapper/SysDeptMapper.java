package com.zhgw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhgw.entity.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {
    
    @Select("SELECT * FROM sys_dept WHERE parent_id = #{parentId} AND del_flag = 0 ORDER BY order_num")
    List<SysDept> selectChildren(Long parentId);
} 