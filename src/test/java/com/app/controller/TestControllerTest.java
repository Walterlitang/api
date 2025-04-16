package com.app.controller;

import com.app.model.Article;
import com.app.service.IArticleService;
import com.app.util.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestParam;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TestControllerTest {

    @Autowired
    private IArticleService articleService;

    @Test
    public void commonWriting(){
        Article article=articleService.getById(1);
        if(article!=null){
            article.setViewCount(article.getViewCount()+1);
            this.articleService.updateById(article);
        }
    }

}