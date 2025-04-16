package com.app.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.app.aspect.AutoLog;
import com.app.common.TotalConfig;
import com.app.model.Banner;
import com.app.service.IBannerService;
import com.app.util.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 轮播图表 前端控制器
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
@Api(tags = "轮播图")
@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private IBannerService bannerService;

    /**
     * 新增或编辑轮播图
     *
     * @param banner 轮播图对象
     * @return 操作结果
     */
    @ApiOperation(value = "新增或编辑轮播图")
    @AutoLog("新增或编辑轮播图")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Banner banner) {
        if (!StpUtil.hasPermission("admin:carouselImage:save")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        try {
            // 输入验证
            if (banner == null) {
                return Result.error("轮播图对象无效");
            }

            boolean savedOrUpdated;
            if (banner.getId() != null) {
                savedOrUpdated = this.bannerService.updateById(banner);
            } else {
                banner.setCreateTime(LocalDateTime.now());
                savedOrUpdated = this.bannerService.save(banner);
            }

            if (savedOrUpdated) {
                return Result.success("轮播图保存或更新成功");
            } else {
                return Result.error("轮播图保存或更新失败");
            }
        } catch (Exception e) {
            return Result.error("系统错误，请稍后再试");
        }
    }

    /**
     * 删除轮播图
     *
     * @param id 轮播图ID
     * @return 操作结果
     */
    @ApiOperation(value = "删除轮播图")
    @AutoLog("删除轮播图")
    @GetMapping("/delete")
    public Result delete(Integer id) {
        if (!StpUtil.hasPermission("admin:carouselImage:delete")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        try {
            // 获取轮播图
            Banner banner = this.bannerService.getById(id);
            if (banner == null) {
                return Result.error("该轮播图不存在");
            }

            // 设置删除标志并更新
            banner.setIsDel(1);
            boolean updateSuccess = this.bannerService.updateById(banner);

            if (updateSuccess) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败，请稍后再试");
            }
        } catch (Exception e) {
            return Result.error("系统错误，请稍后再试");
        }
    }

    /**
     * 根据ID查询轮播图
     *
     * @param id 轮播图ID
     * @return 轮播图对象
     */
    @ApiOperation(value = "查询轮播图详情")
    @GetMapping("/info")
    public Result info(Integer id) {
        return Result.success(this.bannerService.getById(id));
    }

    /**
     * 查询所有轮播图
     *
     * @return 轮播图列表
     */
    @ApiOperation(value = "分页查询轮播图列表")
    @GetMapping("/list")
    public Result page(@RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                       @RequestParam(value = "keyword", required = false) String keyword) {
        if (!StpUtil.hasPermission("admin:carousel-image:list")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        Page<Banner> bannerPage = new Page<>(page, size);
        return Result.success(this.bannerService.adminPage(bannerPage, keyword));
    }

    /**
     * 修改轮播图状态
     *
     * @param id     轮播图ID
     * @param status 轮播图状态
     */
    @ApiOperation(value = "修改轮播图状态")
    @AutoLog("修改轮播图状态")
    @GetMapping("/status")
    public Result updateArticleStatus(Integer id, Integer status) {
        Banner banner = this.bannerService.getById(id);
        if (banner == null) {
            return Result.error("轮播图不存在");
        }
        banner.setStatus(status);
        boolean updated = this.bannerService.updateById(banner);
        if (updated) {
            return Result.success("轮播图状态修改成功");
        } else {
            return Result.error("轮播图状态修改失败");
        }
    }

    /**
     * 查询web页轮播图  查询排序前六张
     *
     * @return 查询web页轮播图
     */
    @ApiOperation(value = "查询web页轮播图")
    @GetMapping("/webPage")
    public Result webPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                          @RequestParam(value = "size", defaultValue = "6") Integer size) {
        Page<Banner> bannerPage = new Page<>(page, size);
        return Result.success(this.bannerService.webPage(bannerPage));
    }
}

