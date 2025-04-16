package com.app.model;

import com.app.conf.LocalDateTimeDeserializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文章内容表
 * </p>
 *
 * @author yoominic
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lm_article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分类id
     */
    @NotNull(message = "分类不能为空")
    @ApiModelProperty(value = "分类id", required = true, example = "1")
    private Integer categoryId;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @ApiModelProperty(value = "标题", required = true, example = "xxx")
    private String title;

    /**
     * 内容
     */
//    @NotBlank(message = "内容不能为空")
    @ApiModelProperty(value = "内容", required = true, example = "xxx")
    private String content;
    /**
     * 封面
     */
    @ApiModelProperty(value = "封面", example = "xxx")
    private String pic;
    /**
     * 摘要
     */
//    @NotBlank(message = "摘要不能为空")
    @ApiModelProperty(value = "摘要", required = true, example = "xxx")
    private String pick;
    /**
     * 文章来源
     */
//    @NotBlank(message = "文章来源不能为空")
    @ApiModelProperty(value = "文章来源", required = true, example = "xxx")
    private String articleSource;
    /**
     * 是否焦关注0否1是
     */
    @ApiModelProperty(value = "是否焦关注0否1是", example = "1")
    private Integer isFocus;
    /**
     * 角色id
     */
    private Integer roleId;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @TableField(exist = false)
    private LocalDateTime releaseTime;

    /**
     * 1正常 0 隐藏 2 待审核
     */
    @ApiModelProperty(value = "1正常 0 隐藏 2 待审核", example = "1")
    private Integer status;

    /**
     * 作者
     */
    @ApiModelProperty(value = "作者", example = "1")
    private String author;

    /**
     * 审核领导
     */
    @ApiModelProperty(value = "审核领导", example = "1")
    private String reviewer;

    /**
     *
     * 外部链接
     */
    @ApiModelProperty(value = "外部链接", example = "xxx")
    private String jumpUrl;

    /**
     *
     * 部职别
     */
    @ApiModelProperty(value = "部职别", example = "xxx")
    private String departmentTitle;

    /**
     *
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "xxx")
    private Integer departmentId;

    @TableField(exist = false)
    private String departmentName;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;

    /**
     * 是否删除1删除0未删除
     */
    private Integer isDel;

    @TableField(exist = false)
    private String categoryName;

    @ApiModelProperty(value = "浏览量", example = "1")
    private Integer viewCount;
}
