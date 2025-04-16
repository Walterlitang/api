package com.app.service.Impl;

import com.app.mapper.VideoMapper;
import com.app.model.Video;
import com.app.service.IVideoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 视频管理表 服务实现类
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements IVideoService {

    @Override
    public IPage<Video> adminPage(Page<Video> videoPage, String title) {
        return this.baseMapper.adminPage(videoPage, title);
    }

    @Override
    public IPage<Video> webList(Page<Video> videoPage, String title) {
        return this.baseMapper.webList(videoPage, title);
    }
}
