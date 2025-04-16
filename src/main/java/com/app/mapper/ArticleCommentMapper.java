package com.app.mapper;

import com.app.model.ArticleComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 文章评论表 Mapper 接口
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
public interface ArticleCommentMapper extends BaseMapper<ArticleComment> {

    IPage<ArticleComment> adminPage(Page<ArticleComment> commentPage, String content, Integer articleId);

    IPage<ArticleComment> webByArticleId(Page<ArticleComment> commentPage, String content, Integer articleId);
}
