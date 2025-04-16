package com.app.service.Impl;

import com.app.mapper.BannerMapper;
import com.app.model.Banner;
import com.app.service.IBannerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 轮播图表 服务实现类
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements IBannerService {

    @Override
    public IPage<Banner> adminPage(Page<Banner> bannerPage, String title) {
        return this.baseMapper.adminPage(bannerPage, title);
    }

    @Override
    public IPage<Banner> webPage(Page<Banner> bannerPage) {
        return this.baseMapper.webPage(bannerPage);
    }
}
