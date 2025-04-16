package com.app.vo;

import com.app.model.Article;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

@Data
public class NewDevelopmentsVo {
    private String name;
    private IPage<Article> articleIPage;

}
