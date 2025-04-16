package com.app.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RegisterRequestVO {

    @NotNull(message = "请选择注册角色")
    private Integer roleId;
    /**
     * 单位名称
     */
    private String departmentName;
    /**
     * 真实姓名
     */
    private String realName;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "key不能为空")
    private String key;

    @NotBlank(message = "验证码不能为空")
    private String code;

}
