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
@TableName("lm_permission")
@JsonInclude(JsonInclude.Include.NON_NULL)

public class PermissionModel {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer pid;//父id，如果没有父级则为0
    private String name;//权限名称
    private String icon;//el-icon图标名称，XX即el-icon-XX
    private String component;//组件路径
    private String perms;//权限字符，如admin:person:list
    private String menuType;//类型，M目录、C菜单、A按钮
    private String url;//url
    private Integer sort;//排序，数字越大越靠前
    private Integer isShow;//是否显示，1显示 0不显示
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private Timestamp updateTime;
    @TableField(exist = false)
    private boolean checked;
    @TableField(exist = false)
    private List<PermissionModel> childList;
}
