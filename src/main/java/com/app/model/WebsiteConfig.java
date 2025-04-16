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

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 网站配置
 * </p>
 *
 * @author yoominic
 * @since 2025-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lm_website_config")
public class WebsiteConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 部队编号
     */
    @ApiModelProperty(value = "部队编号")
    private String unitNumber;

    /**
     * 主办单位
     */
    @ApiModelProperty(value = "主办单位")
    private String theOrganizer;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String contactPhoneNumber;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contactPerson;

    /**
     * 备案号
     */
    @ApiModelProperty(value = "备案号")
    private String recordNumber;

    /**
     * logo
     */
    @ApiModelProperty(value = "logo")
    private String logo;

    /**
     * 首长信箱说明
     */
    @ApiModelProperty(value = "首长信箱说明")
    private String chiefMailboxInstructions;

    /**
     * 纪委信箱说明
     */
    @ApiModelProperty(value = "纪委信箱说明")
    private String disciplineMailboxInstructions;

    /**
     * 背景图片
     */
    @ApiModelProperty(value = "背景图片")
    private String backgroundImage;

    /**
     * 背景颜色
     */
    @ApiModelProperty(value = "背景颜色")
    private String background;

    /**
     * 是否删除1删除0未删除
     */
    @ApiModelProperty(value = "是否删除1删除0未删除")
    private Integer isDel;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;


}
