package com.zhgw.model.param;

import lombok.Data;

@Data
public class PasswordUpdateParam {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
} 