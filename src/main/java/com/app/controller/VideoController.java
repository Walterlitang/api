package com.app.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.app.aspect.AutoLog;
import com.app.common.TotalConfig;
import com.app.model.Video;
import com.app.service.IVideoService;
import com.app.util.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 视频管理表 前端控制器
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
@Api(tags = "视频管理")
@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private IVideoService videoService;

    /**
     * 新增或编辑视频
     *
     * @param video 视频对象
     * @return 操作结果
     */
    @ApiOperation(value = "新增或编辑视频")
    @AutoLog("新增或编辑视频")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Video video) {
        try {
            // 输入验证
            if (video == null) {
                return Result.error("视频对象无效");
            }

            boolean savedOrUpdated;
            if (video.getId() != null) {
                savedOrUpdated = videoService.updateById(video);
            } else {
                video.setCreateTime(LocalDateTime.now());
                savedOrUpdated = videoService.save(video);
            }

            if (savedOrUpdated) {
                return Result.success("视频保存或更新成功");
            } else {
                return Result.error("视频保存或更新失败");
            }
        } catch (Exception e) {
            return Result.error("系统错误，请稍后再试");
        }
    }

    /**
     * 删除视频
     *
     * @param id 视频ID
     * @return 操作结果
     */
    @ApiOperation(value = "删除视频")
    @AutoLog("删除视频")
    @GetMapping("/delete")
    public Result delete(Integer id) {
        try {
            // 获取视频
            Video video = videoService.getById(id);
            if (video == null) {
                return Result.error("该视频不存在");
            }

            // 设置删除标志并更新
            video.setIsDel(1);
            boolean updateSuccess = videoService.updateById(video);

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
     * 根据ID查询视频
     *
     * @param id 视频ID
     * @return 视频对象
     */
    @ApiOperation(value = "查询视频详情")
    @GetMapping("/info")
    public Result info(Integer id) {
        return Result.success(videoService.getById(id));
    }

    /**
     * 查询所有视频
     *
     * @return 视频列表
     */
    @ApiOperation(value = "查询所有视频")
    @GetMapping("/list")
    public Result page(@RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                       @RequestParam(value = "keyword", required = false) String keyword) {
        if (!StpUtil.hasPermission("admin:strong-military-video:list")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        Page<Video> videoPage = new Page<>(page, size);
        return Result.success(this.videoService.adminPage(videoPage, keyword));
    }

    /**
     * 修改视频状态
     *
     * @param id     轮播图ID
     * @param status 轮播图状态
     */
    @ApiOperation(value = "修改视频状态")
    @AutoLog("修改视频状态")
    @PatchMapping("/status")
    public Result updateArticleStatus(Integer id, @RequestParam Integer status) {
        Video video = this.videoService.getById(id);
        if (video == null) {
            return Result.error("轮播图不存在");
        }
        video.setStatus(status);
        boolean updated = this.videoService.updateById(video);
        if (updated) {
            return Result.success("轮播图状态修改成功");
        } else {
            return Result.error("轮播图状态修改失败");
        }
    }

    /**
     * web查询所有视频
     *
     * @return 视频列表
     */
    @ApiOperation(value = "web查询所有视频")
    @GetMapping("/webList")
    public Result webList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                       @RequestParam(value = "title", required = false) String title) {
        Page<Video> videoPage = new Page<>(page, size);
        return Result.success(this.videoService.webList(videoPage, title));
    }
}
