package com.app.activemq;

import com.alibaba.fastjson.JSONObject;
import com.app.model.Article;
import com.app.service.IArticleService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName Consumer
 * @Description TODO
 * @Author yoominic
 * @Date 2022/11/9 11:15 周三
 * @Version 1.0.0
 **/
@Component
@Slf4j
public class Consumer {

    @Autowired
    private IArticleService articleService;

    /**
     * 记录凭证消费凭证
     *
     * @param message 信息
     */
    @JmsListener(destination = "articlesIncreaseAmountReading")
    public void articlesIncreaseAmountReading(String message) {
        try {

            JSONObject jsonObject = JSONObject.parseObject(message);
            Integer id = jsonObject.getInteger("id");


            // 创建 UpdateWrapper 实例
            UpdateWrapper<Article> articleUpdateWrapper = new UpdateWrapper<>();

            // 设置更新条件和操作：viewCount + 1
            articleUpdateWrapper.eq("id", id)
                    .setSql("view_count = view_count + 1");

            // 执行更新操作
            boolean isUpdated = articleService.update(null, articleUpdateWrapper);

        } catch (Exception e) {
            log.error("处理消息时发生错误", e);
        }
    }


}
