package com.zhgw.model.param;

import lombok.Data;

@Data
public class ResetPasswordParam {
    private Long userId;
    private String password;
} 