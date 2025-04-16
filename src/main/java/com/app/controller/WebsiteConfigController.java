package com.app.controller;

import com.app.aspect.AutoLog;
import com.app.model.Video;
import com.app.model.WebsiteConfig;
import com.app.service.IWebsiteConfigService;
import com.app.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 网站配置 前端控制器
 * </p>
 *
 * @author yoominic
 * @since 2025-02-09
 */
@Api(tags = "网站配置")
@RestController
@RequestMapping("/websiteConfig")
public class WebsiteConfigController {

    @Autowired
    private IWebsiteConfigService websiteConfigService;

    /**
     * 新增网站配置
     *
     * @return
     */
    @ApiOperation("新增网站配置")
    @PostMapping("/add")
    public Result addWebsiteConfig(@RequestBody WebsiteConfig websiteConfig) {
        try {
            // 输入验证
            if (websiteConfig == null) {
                return Result.error("网站配置对象无效");
            }

            boolean savedOrUpdated;
            if (websiteConfig.getId() != null) {
                savedOrUpdated = this.websiteConfigService.updateById(websiteConfig);
            } else {
                websiteConfig.setCreateTime(LocalDateTime.now());
                savedOrUpdated = this.websiteConfigService.save(websiteConfig);
            }

            if (savedOrUpdated) {
                return Result.success("网站配置保存或更新成功");
            } else {
                return Result.error("网站配置保存或更新失败");
            }
        } catch (Exception e) {
            return Result.error("系统错误，请稍后再试");
        }
    }

    /**
     * 获取网站配置列表
     *
     * @return
     */
    @ApiOperation("获取网站配置列表")
    @GetMapping("/list")
    public Result getWebsiteConfigList() {
        List<WebsiteConfig> websiteConfigs = websiteConfigService.list();
        return Result.success(websiteConfigs);
    }

    /**
     * 获取网站配置详情
     *
     * @param id
     * @return
     */
    @ApiOperation("获取网站配置详情")
    @GetMapping("/detail")
    public Result getWebsiteConfigDetail(@RequestParam(value = "id", defaultValue = "1") Integer id) {
        return Result.success(websiteConfigService.getById(id));
    }

    /**
     * 删除网站配置
     *
     * @param id 网站配置ID
     * @return 操作结果
     */
    @ApiOperation(value = "删除网站配置")
    @AutoLog("删除网站配置")
    @GetMapping("/delete")
    public Result delete(Integer id) {
        try {
            // 获取网站配置
            WebsiteConfig websiteConfig = websiteConfigService.getById(id);
            if (websiteConfig == null) {
                return Result.error("该网站配置不存在");
            }

            // 设置删除标志并更新
            websiteConfig.setIsDel(1);
            boolean updateSuccess = websiteConfigService.updateById(websiteConfig);

            if (updateSuccess) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败，请稍后再试");
            }
        } catch (Exception e) {
            return Result.error("系统错误，请稍后再试");
        }
    }
}
