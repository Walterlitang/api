package com.app.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName SuperEnumFace
 * @Description TODO
 * @Author yoominic
 * @Date 2022/7/26 15:34 周二
 * @Version 1.0.0
 **/
@Getter
@AllArgsConstructor
public enum SuperEnumFace {

    SUCCESS(200, "操作成功"),

    NO_LOGIN(401, "用户未登录"),

    AUTHORIZED(403, "没有操作权限"),

    NOT_SUPPORTED_REQUEST_METHOD(405, "不支持的请求方式"),

    SYSTEM_ERROR(500, "系统异常"),

    FAIL(400, "操作失败"),

    VALID_ERROR(402, "参数格式不正确");


    private final Integer code;

    private final String msg;

}
