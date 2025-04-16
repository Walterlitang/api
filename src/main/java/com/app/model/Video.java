package com.app.model;

import com.app.conf.LocalDateTimeDeserializer;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * 视频管理表
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lm_video")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @ApiModelProperty(value = "标题", required = true, example = "xxx")
    private String title;

    /**
     * 视频
     */
    @NotBlank(message = "视频不能为空")
    @ApiModelProperty(value = "视频", required = true, example = "xxx")
    private String videoUrl;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;

    /**
     * 状态1显示0隐藏
     */
    @ApiModelProperty(value = "状态1显示0隐藏", example = "1")
    private Integer status;

    /**
     * 是否删除1删除0未删除
     */
    @ApiModelProperty(value = "是否删除1删除0未删除", example = "1")
    private Integer isDel;

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
