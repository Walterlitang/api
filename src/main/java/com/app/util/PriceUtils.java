package com.app.util;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * 价格utils
 *
 * @author yoominic
 * @date 2023/12/26
 */
public class PriceUtils {


    public static Integer changeY2F(Double amount) {
        String currency = amount.toString();
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0L;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
        }
        return amLong.intValue();
    }


    public static Double fenToYuan(Double amount) {
        NumberFormat format = NumberFormat.getInstance();
        try {
            Number number = format.parse(String.valueOf(amount));
            double temp = number.doubleValue() / 100.0;
            format.setGroupingUsed(false);
            // 设置返回的小数部分所允许的最大位数
            format.setMaximumFractionDigits(2);
            amount = Double.valueOf(format.format(temp));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return amount;
    }

}
