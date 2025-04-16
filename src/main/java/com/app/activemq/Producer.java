package com.app.activemq;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Producer
 * @Description TODO
 * @Author yoominic
 * @Date 2022/11/9 11:24 周三
 * @Version 1.0.0
 **/
@Component
@Slf4j
@Service
public class Producer {


    // 也可以注入JmsTemplate，JmsMessagingTemplate对JmsTemplate进行了封装
    @Resource
    private JmsMessagingTemplate jmsTemplate;  //发消息的工具类

    @Async
    public void articlesIncreaseAmountReading(Integer id) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("id", id);
        final String message = JSONObject.toJSONString(responseMap);
        Destination destination = new ActiveMQQueue("articlesIncreaseAmountReading");
        jmsTemplate.convertAndSend(destination, message);
        log.info("发送消息成功:{}", message);
    }


}
