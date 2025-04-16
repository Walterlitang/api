package com.app.service;

import com.app.model.Banner;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 轮播图表 服务类
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
public interface IBannerService extends IService<Banner> {

    IPage<Banner> adminPage(Page<Banner> bannerPage, String title);

    IPage<Banner> webPage(Page<Banner> bannerPage);
}
