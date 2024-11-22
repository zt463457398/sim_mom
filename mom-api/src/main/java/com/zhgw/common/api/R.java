package com.zhgw.common.api;

import lombok.Data;

@Data
public class R<T> {
    private int code;
    private String msg;
    private T data;

    private R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> R<T> ok(T data) {
        return new R<>(200, "success", data);
    }

    public static <T> R<T> fail(String msg) {
        return new R<>(500, msg, null);
    }
} 