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
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 友情链接表
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lm_friendship_link")
public class FriendshipLink implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 分类id
     */
    @NotBlank(message = "请选择分类")
    @ApiModelProperty(value = "分类id", required = true, example = "1")
    private Integer cId;

    /**
     * 链接地址
     */
//    @NotBlank(message = "链接地址不能为空")
    @ApiModelProperty(value = "链接地址", required = true, example = "xxx")
    private String linkAddress;

    /**
     * 状态1显示0隐藏
     */
    @ApiModelProperty(value = "状态1显示0隐藏", example = "1")
    private Integer status;

    /**
     * 是否删除1删除0未删除
     */
    @ApiModelProperty(value = "是否删除1删除0未删除", example = "0")
    private Integer isDel;

    /**
     * 图标
     */
    @ApiModelProperty(value = "图标")
    private String icon;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", example = "0")
    private Integer sort;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String categoryName;

}
