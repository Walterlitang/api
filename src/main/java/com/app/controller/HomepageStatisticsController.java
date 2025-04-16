package com.app.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.app.model.Article;
import com.app.model.Banner;
import com.app.model.Mailbox;
import com.app.model.Video;
import com.app.service.IArticleService;
import com.app.service.IBannerService;
import com.app.service.IMailboxService;
import com.app.service.IVideoService;
import com.app.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/homepageStatistics")
public class HomepageStatisticsController {

    private static final Logger log = LoggerFactory.getLogger(HomepageStatisticsController.class);
    @Autowired
    private IMailboxService letterService;

    @Autowired
    private IArticleService articleService;

    @Autowired
    private IBannerService bannerService;

    @Autowired
    private IVideoService videoService;

    /**
     * 查询当前登录人收到的信件数、文章总数、轮播图总数、强军视频总数
     */
    @ApiOperation(value = "查询当前登录人收到的信件数、文章总数、轮播图总数、强军视频总数")
    @GetMapping("/getStatistics")
    public Result getStatistics() {
        try {
            // 获取当前登录人的ID
            Integer loginId = StpUtil.getLoginIdAsInt();

            // 查询当前登录人收到的信件数
            QueryWrapper<Mailbox> letterQueryWrapper = new QueryWrapper<>();
            letterQueryWrapper.eq("user_id", loginId);
            letterQueryWrapper.eq("is_del", 0);
            int letterCount = letterService.count(letterQueryWrapper);

            // 查询文章总数
            QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
            articleQueryWrapper.eq("is_del", 0);
            int articleCount = articleService.count();

            // 查询轮播图总数
            QueryWrapper<Banner> bannerQueryWrapper = new QueryWrapper<>();
            bannerQueryWrapper.eq("is_del", 0);
            int carouselCount = bannerService.count();

            // 查询强军视频总数
            QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
            videoQueryWrapper.eq("is_del", 0);
            int strongMilitaryVideoCount = videoService.count();

            // 封装统计数据
            Map<String, Integer> statistics = new HashMap<>();
            statistics.put("letterCount", letterCount);
            statistics.put("articleCount", articleCount);
            statistics.put("carouselCount", carouselCount);
            statistics.put("strongMilitaryVideoCount", strongMilitaryVideoCount);

            return Result.success(statistics);
        } catch (Exception e) {
            log.error(String.valueOf(e));
            return Result.error("系统错误，请稍后再试");
        }
    }
}
