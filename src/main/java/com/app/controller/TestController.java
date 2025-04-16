package com.app.controller;

import com.app.activemq.Producer;
import com.app.model.Article;
import com.app.service.IArticleService;
import com.app.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private IArticleService articleService;

    private static final AtomicInteger successCounter = new AtomicInteger(0);

    @Autowired
    private Producer producer;

    /**
     * 常见写法
     *
     * @param id id
     */
    @GetMapping("commonWriting")
    public Result commonWriting(@RequestParam("id") Integer id) {
        Article article = articleService.getById(id);
        if (article != null) {
            article.setViewCount(article.getViewCount() + 1);
            this.articleService.updateById(article);
        }
        return Result.success(article);
    }


    @GetMapping("messageQueue")
    public Result messageQueue(@RequestParam("id") Integer id) {
        Article article = articleService.getById(id);
        if (article != null) {
            this.producer.articlesIncreaseAmountReading(id);
        }
        return Result.success(article);
    }


}
