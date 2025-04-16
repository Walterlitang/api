package com.app.handler;

import cn.dev33.satoken.exception.NotLoginException;
import com.app.common.SuperEnumFace;
import com.app.exception.BaseException;
import com.app.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ClassName ExceptionHandlerAdvice
 * @Description TODO
 * @Author yoominic
 * @Date 2022/7/26 15:39 周二
 * @Version 1.0.0
 **/
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    /**
     * 拦截登录异常
     *
     * @param e e
     * @return {@link Object}
     * @throws Exception 例外
     */
    @ExceptionHandler(NotLoginException.class)
    public Object NotLoginException(NotLoginException e) {
        log.error("token异常：{}", e.getMessage());
        Result result = new Result(401, "登录失效");

        return result;
    }
    /**
     * 异常处理程序
     *
     * @param e e
     * @return {@link Result}
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public Result exceptionHandler(BaseException e) {
        log.error("业务异常：{}", e.getMessage());
        Result result = new Result(e.getCode(), e.getMessage());
        return result;
    }


    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public Result MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        log.error("业务异常：{}", e.getMessage());
        Result result = new Result(400, "缺少参数");
        return result;
    }


    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public Result NullPointerException(NullPointerException e) {
        e.printStackTrace();
        log.error("业务异常：{}", e.getMessage());
        Result result = new Result(500, "系统开小差啦～");
        return result;
    }




    /**
     * 方法参数无效异常
     *
     * @param e e
     * @return {@link Object}
     * @throws Exception 例外
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("业务异常：{}", e.getMessage());
        Result result = new Result(400, e.getBindingResult().getFieldError().getDefaultMessage());
        return result;
    }

    @ExceptionHandler(Exception.class)
    public Object Exception(Exception e) {
        e.printStackTrace();
        Result result = new Result(SuperEnumFace.SYSTEM_ERROR.getCode(), SuperEnumFace.SYSTEM_ERROR.getMsg());
        return result;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object HttpRequestMethodNotSupportedException(Exception e) {
        e.printStackTrace();
        Result result = new Result(SuperEnumFace.NOT_SUPPORTED_REQUEST_METHOD.getCode(), SuperEnumFace.NOT_SUPPORTED_REQUEST_METHOD.getMsg());
        return result;
    }


}
