package com.app.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassDescription: 公共帮助类
 * @Author: N
 * @ModifyDescription:
 */
public class CommonHelp {


    /**
     * @MethodDescription: 时间解释方法
     * @Author: N
     * @ModifyDescription:
     */
    public static String explainTime(long timeDistance) {
        StringBuilder sb = new StringBuilder();
        // if(timeDistance/3600000!=0){
        // sb.append(timeDistance/3600000+"小时");
        // timeDistance = timeDistance%3600000;
        // }
        if (timeDistance / 60000 != 0) {
            sb.append(timeDistance / 60000 + "分钟");
            timeDistance = timeDistance % 60000;
        }
        if (timeDistance / 1000 != 0) {
            sb.append(timeDistance / 1000 + "秒钟");
            timeDistance = timeDistance % 1000;
        }
        return sb.toString();
    }

    /**
     * @MethodDescription: 时间解释方法
     * @Author: N
     * @ModifyDescription:
     */
    public static long explainTime(String timeDistance) {
        int minute = 0;
        int sec = 0;
        if (timeDistance.indexOf("分钟") != -1) {
            minute = Integer.valueOf(timeDistance.substring(timeDistance.indexOf("分钟")));
            sec = Integer.valueOf(timeDistance.substring(timeDistance.indexOf("分钟"), timeDistance.indexOf("秒")));
        }
        System.out.println();
        return 0000000;
    }


    /**
     * @MethodDescription: 得到当前timestamp
     * @Author: N
     * @ModifyDescription:
     */
    public static Timestamp getNowTimestamp() {
        return new Timestamp((new Date()).getTime());
    }

    /**
     * @param @param  beforeOrBeAfter 前或者后
     * @param @param  second 变动的时间（单位：秒）
     * @param @return
     * @return Timestamp
     * @throws
     * @Title: getBeforeOrAfterTimestamp
     * @Description: TODO 获取当前时间前后时间
     */
    public static Timestamp getBeforeOrAfterTimestamp(Boolean beforeOrAfter, int second) {
        long time = (new Date()).getTime();
        // before
        if (beforeOrAfter) {
            time = time - second * 1000;
        } else { // after
            time = time + second * 1000;
        }
        return new Timestamp(time);
    }

    /**
     * @param @param  beforeOrAfter
     * @param @param  second
     * @param @param  tm
     * @param @return
     * @return Timestamp
     * @Title: getBeforeOrAfterTimestamp
     * @Description: TODO
     */
    public static Timestamp getBeforeOrAfterTimestamp(Boolean beforeOrAfter, int second, Timestamp tm) {
        long time = tm.getTime();
        // before
        if (beforeOrAfter) {
            time = time - second * 1000;
        } else { // after
            time = time + second * 1000;
        }
        return new Timestamp(time);
    }

    /**
     * @param @return
     * @return String
     * @Title: getNowDate
     * @Description: TODO 获取当前日期字符串
     */
    public static String getNowDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String currentDate = dateFormat.format(new Date());
        return currentDate;
    }

    /**
     * ************************************************************************
     * TODO 方法getMinuteDec的简要说明 <br><pre>
     * 获取与当前时间的分钟差值 <br>
     * *************************************************************************
     */
    public static int getMinuteDec(String strTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        int res = 0;
        try {
            date = format.parse(strTime);
            // 当前时间
            Date curTime = new Date();
            // 获取差值  当前时间-计算时间
            long cc = curTime.getTime() - date.getTime();
            res = Math.abs((int) (cc / 1000) / 60);
        } catch (ParseException e) {
            e.printStackTrace();
            res = 10;
        }
        return res;
    }

    /**
     * @MethodDescription: 获取唯一的GUID
     * @ModifyDescription:
     */
    public static String getGUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * @MethodDescription: 获取真实的远程IP地址
     * @Author: N
     */
    public static String getRemoteIp() {
        InetAddress addr;
        try {
            addr = InetAddress.getLocalHost();
            return addr.getHostAddress().toString();
        } catch (UnknownHostException e) {
            return "0.0.0.0";
        }
    }


    /**
     * ****************************
     * 说明:转码
     * 作者:N
     * <p>
     * ****************************
     */
    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U'))) {
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                } else {
                    retBuf.append(unicodeStr.charAt(i));
                }
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }

    //冒泡排序
    public static Double bubbleSort(Double[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {//外层循环控制排序趟数
            for (int j = 0; j < arr.length - 1 - i; j++) {//内层循环控制每一趟排序多少次
                if (arr[j] > arr[j + 1]) {
                    Double temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        return arr[0];
    }

    public static Map<String, String> getProvincialAndMunicipalLevel(String areacde) {
        Map<String, String> resultMap = new HashMap<String, String>();
        String area = areacde;
        String regArea = null;
        String regCity = null;
        String regProvince = null;
        // 深圳另外算
        if (!"440300".equals(areacde)) {
            if (!"00".equals(area.substring(4, 6))) {
                regArea = areacde;
                regCity = regArea.substring(0, 4) + "00";
                regProvince = regCity.substring(0, 2) + "0000";
            } else {
                if (!"00".equals(area.substring(2, 4))) {
                    regArea = null;
                    regCity = areacde.substring(0, 4) + "00";
                    regProvince = regCity.substring(0, 2) + "0000";
                } else {
                    regArea = null;
                    regCity = null;
                    regProvince = areacde;
                }
            }
        } else {
            // 深圳
            regArea = null;
            regCity = null;
            regProvince = areacde;
        }
        resultMap.put("regProvince", regProvince);
        resultMap.put("regCity", regCity);
        resultMap.put("regArea", regArea);
        return resultMap;
    }

}
