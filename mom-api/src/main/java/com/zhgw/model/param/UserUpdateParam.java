package com.zhgw.model.param;

import lombok.Data;

@Data
public class UserUpdateParam {
    private Long id;
    private String realName;
    private String phone;
    private String email;
    private Integer status;
} 