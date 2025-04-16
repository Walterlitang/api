package com.app.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TotalConfig {
    //支付的状态
    public static String PENDING_PAYMENT = "待支付";
    public static String PAID_ALREADY = "已支付";
    public static String PAYMENT_FAILED = "支付失败";


    //PaymentRecordsLog表type字段
    public static String GAS_COST = "燃气费";

    //您暂无此权限
    public static String NO_PERMISSION = "您暂无此权限";

}
