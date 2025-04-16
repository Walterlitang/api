package com.app.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusCodeEnum {
    STATUS200(200, "操作成功"),
    STATUS401(401, "token不存在"),
    STATUS403(403, "无权访问"),
    STATUS406(406, "用户不存在");

    private final Integer code;
    private final String msg;
}

