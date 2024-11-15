package com.zhgw.model.vo;

import lombok.Data;

@Data
public class UserInfoVO {
    private Long id;
    private String username;
    private String realName;
    private String phone;
    private String email;
    private String avatar;
    private Long deptId;
    private String deptName;
    private Long roleId;
    private String roleName;
    private Integer status;
} 