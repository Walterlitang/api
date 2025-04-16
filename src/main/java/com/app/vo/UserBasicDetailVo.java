package com.app.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UserBasicDetailVo {
    private Integer id;
    /**
     * 昵称
     */
    private String name;

    /**
     * 头像
     */
//    private String faceUrl;
    /**
     * 电话
     */
    private String mobile;
    @ApiModelProperty(value = "缴费总金额")
    private BigDecimal totalPaymentAmount;

    private List<UserBasicGasDetailVo> gasDetailVos;

}
