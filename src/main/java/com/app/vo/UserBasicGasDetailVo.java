package com.app.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserBasicGasDetailVo {
    /**
     * 户号
     */
    @ApiModelProperty(value = "户号")
    private String account;
    @ApiModelProperty(value = "用户名")
    private String userName;

    /**
     * 缴费单位名称
     */
    @ApiModelProperty(value = "缴费单位名称")
    private String companyName;
    /**
     * 燃气表详细地址
     */
    @ApiModelProperty(value = "燃气表详细地址")
    private String personnelInformationAddress;

    @ApiModelProperty(value = "总气量")
    private BigDecimal gasMeterTotal;
}
