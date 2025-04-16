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
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 公用数据配置
 * </p>
 *
 * @author yoominic
 * @since 2023-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lm_sys_category")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父级id
     */
    private Integer pid;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态 1有效0无效
     */
    private boolean status;

    /**
     * 是否导航1是0否
     */
    private Integer isNavigation;

    /**
     * 是否文章1是0否
     */
    private Integer isArticle;

    /**
     * 是否统计1是0否
     */
    private Integer isCounted;

    /**
     * url路径
     */
    private String urlPath;

    /**
     * 背景图片
     */
    private String backgroundImage;

    /**
     * 说明
     */
    private String messageExplanation;

    /**
     * 类型编码
     */
    private String code;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private Date updateTime;
    @TableField(exist = false)
    private List<SysCategory> child;

    @TableField(exist = false)
    private String linkAddress;

    @TableField(exist = false)
    private String icon;

    @TableField(exist = false)
    private String title;

    @TableField(exist = false)
    private Integer fid;

}
