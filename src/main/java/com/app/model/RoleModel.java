package com.app.model;


import com.app.conf.LocalDateTimeDeserializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lm_role")
@JsonInclude(JsonInclude.Include.NON_NULL)

public class RoleModel {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String roleName;//角色名称
    private String rules;//管理权限
    private Integer status;//状态1正常0隐藏
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private Timestamp updateTime;

    @TableField(exist = false)
    private Integer roles;//角色ID
    @TableField(exist = false)
    private String createTimeText;
    @TableField(exist = false)
    private String updateTimeText;
    @TableField(exist = false)
    private String levelText;
    @TableField(exist = false)
    private List<PermissionModel> menuList;
}
