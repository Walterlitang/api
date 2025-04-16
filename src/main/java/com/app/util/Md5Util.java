package com.app.util;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Md5Util {

    /**
     * 生成MD5加密字符串
     *
     * @return MD5加密后的字符串
     */
    public String generateMD5(String str) {
        try {
            // 创建一个MessageDigest实例，初始化为MD5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将输入的字符串进行MD5加密
            // 注意：字符串要转换为字节数组
            md.update((str).getBytes("UTF-8"));

            // 完成哈希计算，得到字节数组
            byte[] digest = md.digest();

            // 将得到的字节数组转换成十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            // 返回十六进制字符串
            return hexString.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
