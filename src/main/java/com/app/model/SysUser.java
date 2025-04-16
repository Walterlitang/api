package com.app.model;

import com.app.conf.LocalDateTimeDeserializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author yoominic
 * @since 2023-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lm_sys_user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账号
     */
    private String username;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 部门ID
     */
    private Integer departmentId;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 角色ID
     */
    private Integer roleId;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 身份角色名称  角色表姓名
     */
    @TableField(exist = false)
    private String roleName;
    /**
     * 最近登录ip
     */
    private String lastLoginIp;

    /**
     * 咨询师id
     */
    private Integer personId;

    /**
     * 最近登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastLoginTime;

    /**
     * 管理员状态  1有效2无效
     */
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    private String phone;

    private String salt;

    /**
     * 职务
     */
    private String position;
    /**
     * 头像
     */
    private String profilePicture;

    @TableField(exist = false)
    private String token;
    @TableField(exist = false)
    private List<String> permissionList;//权限数组
    /**
     * 帖子数量
     */
    @TableField(exist = false)
    private Integer letterCount;
    /**
     * 回复数量
     */
    @TableField(exist = false)
    private Integer repliesCount;
}
