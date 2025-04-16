package com.app.service.Impl;

import com.app.mapper.ArticleCommentMapper;
import com.app.model.ArticleComment;
import com.app.model.Banner;
import com.app.service.IArticleCommentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章评论表 服务实现类
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment> implements IArticleCommentService {

    @Override
    public IPage<ArticleComment> byArticleId(Page<ArticleComment> commentPage, String content, Integer articleId) {
        return this.baseMapper.adminPage(commentPage, content,articleId);
    }

    @Override
    public IPage<ArticleComment> webByArticleId(Page<ArticleComment> commentPage, String content, Integer articleId) {
        return this.baseMapper.webByArticleId(commentPage, content,articleId);
    }
}
