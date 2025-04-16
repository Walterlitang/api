package com.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lm_admin_user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminUserModel {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String account;
    private String pwd;
    private String realName;
    private Integer roles;
    private String lastIp;//最近登录ip
    private Timestamp lastLoginTime;
    private Integer status;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String salt;
    @TableField(exist = false)
    private String token;
    @TableField(exist = false)
    private List<String> permissionList;//权限数组
    @TableField(exist = false)
    private String RoleName;//角色名称
    @TableField(exist = false)
    private String lastLoginTimeText;
}
