package com.app.service;

import com.app.model.Video;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 视频管理表 服务类
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
public interface IVideoService extends IService<Video> {

    IPage<Video> adminPage(Page<Video> videoPage, String title);

    IPage<Video> webList(Page<Video> videoPage, String title);
}
