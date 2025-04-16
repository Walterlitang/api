package com.app.exception;

import com.app.common.SuperEnumFace;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName BaseException
 * @Description TODO
 * @Author yoominic
 * @Date 2022/7/26 15:35 周二
 * @Version 1.0.0
 **/
@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {

    private final String message;
    private Integer code = SuperEnumFace.FAIL.getCode();

    public BaseException(String message) {
        this.message = message;
    }

    public BaseException(SuperEnumFace statusCodeEnum) {
        this.code = statusCodeEnum.getCode();
        this.message = statusCodeEnum.getMsg();
    }

    public BaseException(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }
}
