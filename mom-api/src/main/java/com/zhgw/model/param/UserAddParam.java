package com.zhgw.model.param;

import lombok.Data;

@Data
public class UserAddParam {
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    private Integer status;
} 