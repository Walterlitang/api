package com.app.service.Impl;

import com.app.model.Comment;
import com.app.mapper.CommentMapper;
import com.app.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 动态评论与回复表 服务实现类
 * </p>
 *
 * @author yoominic
 * @since 2025-02-28
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
