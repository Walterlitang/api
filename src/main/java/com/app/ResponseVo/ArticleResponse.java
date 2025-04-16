package com.app.ResponseVo;

import com.app.model.Article;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 招商引资项目 搜索Request
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<IPage<Article>> pages;
    /**
     * 时政资讯列表
     */
    private IPage<Article> currentPoliticsPages;

    /**
     * 焦点关注列表
     */
    private IPage<Article> focusPages;

    /**
     * 统帅足音列表
     */
    private IPage<Article> footPages;

    /**
     * 军委要闻
     */
    private IPage<Article> newsPages;

    /**
     * 火箭军快讯
     */
    private IPage<Article> rocketPages;

    /**
     * 学习课堂
     */
    private IPage<Article> studyPages;

    /**
     * 理论讲堂
     */
    private IPage<Article> theoryPages;

    /**
     * 精品微课
     */
    private IPage<Article> coursePages;

    /**
     * 砺剑河西
     */
    private IPage<Article> liJianHeiXiPages;

    /**
     * 东风论剑
     */
    private IPage<Article> dongFengLunJianPages;

    /**
     * 军情纵横
     */
    private IPage<Article> junQingZongHengPages;

    /**
     * 军事工作
     */
    private IPage<Article> junShiGongZuoPages;

    /**
     * 政治工作
     */
    private IPage<Article> zhengZhiGongZuoPages;

    /**
     * 保障工作
     */
    private IPage<Article> baoZhangGongZuoPages;

    /**
     * 律师在线
     */
    private IPage<Article> lvShiZaiXianPages;

    /**
     * 心灵驿站
     */
    private IPage<Article> xinLingYiZhanPages;

    /**
     * 健康问诊
     */
    private IPage<Article> jianKangWenZhenPages;

    /**
     * 祁连新动态
     */
    // 存储每个子集的文章
    private List<SubCategoryArticles> subCategoryArticles = new ArrayList<>();

    public void addSubCategoryArticles(Integer categoryId, String categoryName, IPage<Article> articles) {
        SubCategoryArticles subCategory = new SubCategoryArticles(categoryId, categoryName, articles);
        this.subCategoryArticles.add(subCategory);
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public static class SubCategoryArticles {
        private Integer categoryId;
        private String categoryName;
        private IPage<Article> articles;

        public SubCategoryArticles(Integer categoryId, String categoryName, IPage<Article> articles) {
            this.categoryId = categoryId;
            this.categoryName = categoryName;
            this.articles = articles;
        }
    }
}
