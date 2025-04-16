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
 * 信箱管理表
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lm_mailbox")
public class Mailbox implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 发送者id
     */
    private Integer addresserId;

    @TableField(exist = false)
    private String userAdminName;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @ApiModelProperty(value = "标题", required = true, example = "xxx")
    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    @ApiModelProperty(value = "内容", required = true, example = "xxx")
    private String content;

    /**
     * 是否公开1是0否
     */
    @NotBlank(message = "是否公开不能为空")
    @ApiModelProperty(value = "是否公开1是0否", required = true, example = "1")
    private Integer isPublicly;

    /**
     * 是否回复1是0否
     */
    @ApiModelProperty(value = "是否回复1是0否", example = "0")
    private Integer isReply;

    /**
     * 回复内容
     */
    @ApiModelProperty(value = "回复内容", example = "xxx")
    private String replyContent;

    /**
     * 回复时间
     */
    @ApiModelProperty(value = "回复时间", example = "xxx")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime replyTime;

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

    @ApiModelProperty(value = "1首长2纪委")
    private Integer type;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String profilePicture;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "信箱分类id")
    private Integer categoryId;

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


}
