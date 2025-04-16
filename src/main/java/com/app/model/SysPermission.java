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
 * 权限
 * </p>
 *
 * @author yoominic
 * @since 2023-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lm_sys_permission")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父id，如果没有父级则为0
     */
    private Integer pid;

    /**
     * 权限名称
     */
    private String name;

    /**
     * el-icon图标名称
     */
    private String icon;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 权限字符,如admin:person;list
     */
    private String perms;

    /**
     * 类型，M目录。C菜单，A按钮
     */
    private String menuType;

    /**
     * url
     */
    private String url;

    /**
     * 排序，数字越大越靠前
     */
    private Integer sort;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private boolean checked;
    @TableField(exist = false)
    private List<SysPermission> childList;
}
