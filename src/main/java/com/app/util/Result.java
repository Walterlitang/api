package com.app.util;

import cn.hutool.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Result
 * @Description TODO
 * @Author yoominic
 * @Date 2022/7/26 15:38 周二
 * @Version 1.0.0
 **/

@Data
@AllArgsConstructor
public class Result<T> implements Serializable {


    /**
     * 状态码
     */
    private int code;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }

    /**
     * 服务器错误
     *
     * @param message
     * @return
     */
    public static Result error(String message) {
        return new Result(HttpStatus.HTTP_BAD_REQUEST, message);
    }

    /**
     * @param data
     * @return
     */
    public static <T> Result success(T data) {
        return new Result(HttpStatus.HTTP_OK, data);
    }


}
