package com.app.mapper;

import com.app.model.Banner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 轮播图表 Mapper 接口
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
public interface BannerMapper extends BaseMapper<Banner> {

    IPage<Banner> adminPage(Page<Banner> bannerPage, String title);

    IPage<Banner> webPage(Page<Banner> bannerPage);
}
