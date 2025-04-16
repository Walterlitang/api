package com.app.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.app.common.WXConstant;
import com.app.pojo.SendModel;
import com.app.service.RedisService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WechatUtil {

    @Resource
    private static RedisService redisService = SpringUtils.getBean(RedisService.class);

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 获得移动
     *
     * @param mobileCode 移动代码
     * @return {@link String}
     */
    public static String getMobile(String mobileCode) {
        //获取access_token
        String token = redisToken();
        // 使用前端code获取手机号码 参数为json格式
        String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + token;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("code", mobileCode);
        String response = HttpUtil.post(url, JSONUtil.toJsonStr(paramMap));
        log.info("获取用户手机号 响应:{}", response);
        cn.hutool.json.JSONObject entries = JSONUtil.parseObj(response);
        String phone = entries.getJSONObject("phone_info").getStr("phoneNumber");
        return phone;
    }

    /**
     * 获取openid
     *
     * @param code 代码
     * @return {@link String}
     */

    public static JSONObject getOpenid(String code) {
        //请求封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("appid", WXConstant.APPID);
        map.put("secret", WXConstant.SECRET);
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpUtil.get(WX_LOGIN, map);
        log.info("请求获取openid:" + json);
        JSONObject jsonObject = JSON.parseObject(json);
        JSONObject object = new JSONObject();
        object.put("openid", jsonObject.getString("openid"));
        if (jsonObject.containsKey("unionid")) {
            object.put("unionid", jsonObject.getString("unionid"));
        }
        return jsonObject;
    }

    /**
     * 发送通用订阅消息
     * 调用微信开放接口subscribeMessage.send发送订阅消息    通用类
     * POST https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=ACCESS_TOKEN
     *
     * @param params params
     * @param data   数据
     * @return {@link SendModel}
     */
    public static SendModel sendCommonSubscribeMessage(Map<String, Object> params, JSONObject data) {
        HttpURLConnection httpConn = null;
        InputStream is = null;
        BufferedReader rd = null;
        String accessToken = null;
        String str = null;
        SendModel sendModel = new SendModel();
        try {
            //获取token  小程序全局唯一后台接口调用凭据
            accessToken = redisToken();
            JSONObject xmlData = new JSONObject();
            //接收者（用户）的 openid
            xmlData.put("touser", params.get("touser"));
            //所需下发的订阅模板id
            xmlData.put("template_id", params.get("template_id"));
            //点击模板卡片后的跳转页面，仅限本小程序内的页面
            xmlData.put("page", params.get("page"));
            //跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
            xmlData.put("miniprogram_state", params.get("miniprogram_state"));
            //进入小程序查看”的语言类型，支持zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)，默认为zh_CN返回值
            xmlData.put("lang", "zh_CN");
            //小程序模板数据
            xmlData.put("data", data);
            sendModel.setXmlData(xmlData);
            URL url = new URL("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestProperty("Host", "https://api.weixin.qq.com");
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=\"UTF-8\"");
            httpConn.setRequestMethod("POST");
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            OutputStream out = httpConn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            osw.write(xmlData.toString());
            osw.flush();
            osw.close();
            out.close();
            is = httpConn.getInputStream();
            rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            sendModel.setStr("返回数据：" + rd.readLine());
            while ((str = rd.readLine()) != null) {
                System.out.println("返回数据：" + str);
            }
        } catch (Exception e) {
            System.out.println("发送模板消息失败.." + e.getMessage());
            if (e.getMessage() != null) {
                sendModel.setE("发送模板消息失败.." + e.getMessage());
            }
        }
        return sendModel;
    }

    /**
     * redis获取token
     */
    public static String redisToken() {
        if (!redisService.hasKey("access_token")) {
            String token_url = "https://api.weixin.qq.com/cgi-bin/stable_token";
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("grant_type", "client_credential");
            parameters.put("appid", WXConstant.APPID);
            parameters.put("secret", WXConstant.SECRET);
            parameters.put("force_refresh", false);
            JSONObject token = JSON.parseObject(HttpUtil.post(token_url, JSONUtil.toJsonStr(parameters)));
            log.info("获取token,{}", token);
            redisService.set("access_token", token.getString("access_token"), token.getLong("expires_in"));
            return token.getString("access_token");
        } else {
            return (String) redisService.get("access_token");
        }
    }


}
