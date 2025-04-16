package com.app.service;

import com.app.model.ArticleComment;
import com.app.model.Banner;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 文章评论表 服务类
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
public interface IArticleCommentService extends IService<ArticleComment> {

    IPage<ArticleComment> byArticleId(Page<ArticleComment> commentPage, String content, Integer articleId);

    IPage<ArticleComment> webByArticleId(Page<ArticleComment> commentPage, String content, Integer articleId);
}
