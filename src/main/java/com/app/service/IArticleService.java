package com.app.service;

import com.app.ResponseVo.ArticleResponse;
import com.app.ResponseVo.CategoryCountResponse;
import com.app.model.Article;
import com.app.model.SysCategory;
import com.app.model.SysUser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文章内容表 服务类
 * </p>
 *
 * @author yoominic
 * @since 2025-01-21
 */
public interface IArticleService extends IService<Article> {

    IPage<Article> webPage(Page<Article> page, String title, Integer cid, String startTime, String endTime);

    IPage<Article> adminPage(Page<Article> articlePage, String title, Integer cid, String startTime, String endTime, SysUser sysUser,Integer departmentId,Integer status);

    ArticleResponse FocusAndCurrentPolitics(Page<Article> focusPage, Page<Article> currentPoliticsPage);

    ArticleResponse StrongMilitaryInformation(Page<Article> generalPage);

    List<SysCategory> NewDevelopments();

    List<CategoryCountResponse> getCategoryCounts(String startTime, String endTime);


    Article getInfo(Integer id);

    List<Article> getCurrentPoliticsHeadline();

}
