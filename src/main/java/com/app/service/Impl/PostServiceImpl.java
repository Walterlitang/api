package com.app.service.Impl;

import com.app.model.Post;
import com.app.mapper.PostMapper;
import com.app.service.IPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 人员动态表 服务实现类
 * </p>
 *
 * @author yoominic
 * @since 2025-02-28
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {

}
