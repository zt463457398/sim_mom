package com.zhgw.model.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    
    private String username;
    
    private String password;
    
    private String realName;
    
    private String phone;
    
    private String email;
    
    private Long deptId;
    
    private Long roleId;
    
    private Integer status;
    
    private String remark;
} 