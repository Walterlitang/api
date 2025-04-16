package com.app.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.app.RequestVo.ArticleSearchRequest;
import com.app.ResponseVo.CategoryCountResponse;
import com.app.aspect.AutoLog;
import com.app.common.TotalConfig;
import com.app.model.Article;
import com.app.model.SysUser;
import com.app.service.IArticleService;
import com.app.service.ISysUserService;
import com.app.util.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 文章内容表 前端控制器
 * </p>
 *
 * @author yoominic
 * @since 2025-01-21
 */
@Slf4j
@Api(tags = "文章内容")
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private IArticleService articleService;
    @Autowired
    private ISysUserService iSysUserService;


    /**
     * 管理后台分页查询文章列表
     *
     * @param page  页码
     * @param size  每页大小
     * @param title 标题
     * @param cid   分类ID
     */
    @ApiOperation(value = "管理后台分页查询文章列表")
    @GetMapping("adminPage")
    public Result adminPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                            @RequestParam(value = "size", defaultValue = "10") Integer size,
                            @RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "departmentId", required = false) Integer departmentId,
                            @RequestParam(value = "cid", required = false) Integer cid,
                            @RequestParam(value = "status", required = false) Integer status,
                            @RequestParam(value = "startTime", required = false) String startTime,
                            @RequestParam(value = "endTime", required = false) String endTime) {
        if (!StpUtil.hasPermission("admin:content:list")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        Integer loginId = StpUtil.getLoginIdAsInt();
        SysUser sysUser = iSysUserService.getById(loginId);
        if (sysUser == null) {
            return Result.error("用户未登录");
        }
        Page<Article> articlePage = new Page<>(page, size);
        return Result.success(this.articleService.adminPage(articlePage, title, cid, startTime, endTime, sysUser,departmentId,status));
    }

    /**
     * web分页查询文章列表
     *
     * @param page  页码
     * @param size  每页大小
     * @param title 标题
     * @param cid   分类ID
     */
    @ApiOperation(value = "web分页查询文章列表")
    @GetMapping("page")
    public Result webPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                          @RequestParam(value = "size", defaultValue = "10") Integer size,
                          @RequestParam(value = "title", required = false) String title,
                          @RequestParam(value = "cid", required = false) Integer cid,
                          @RequestParam(value = "startTime", required = false) String startTime,
                          @RequestParam(value = "endTime", required = false) String endTime) {
        Page<Article> articlePage = new Page<>(page, size);
        return Result.success(this.articleService.webPage(articlePage, title, cid, startTime, endTime));
    }

    /**
     * 新增或编辑文章
     *
     * @param article 文章对象
     */
    @ApiOperation(value = "新增或编辑文章")
    @PostMapping
    @AutoLog("新增或编辑文章")
    public Result saveOrUpdateArticle(@Valid @RequestBody Article article) {
        if (!StpUtil.hasPermission("admin:content:save")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        boolean savedOrUpdated = false;
        Integer loginId = StpUtil.getLoginIdAsInt();
        SysUser sysUser = iSysUserService.getById(loginId);
        if (sysUser == null) {
            return Result.error("用户未登录");
        }
        article.setRoleId(sysUser.getRoleId());
        article.setUserId(sysUser.getId());
        if (article.getId() != null) {
            savedOrUpdated = articleService.updateById(article);
        } else {
            if (sysUser.getRoleId()==1){
                article.setStatus(1);
            }
            article.setAuthor(sysUser.getUsername());
            article.setCreateTime(LocalDateTime.now());
            savedOrUpdated = articleService.save(article);
        }
        if (savedOrUpdated) {
            return Result.success("文章保存或更新成功");
        } else {
            return Result.error("文章保存或更新失败");
        }
    }

    /**
     * 删除文章
     *
     * @param id 文章ID
     */
    @ApiOperation(value = "删除文章")
    @GetMapping("delete")
    @AutoLog("删除文章")
    public Result deleteArticle(Integer id) {
        if (!StpUtil.hasPermission("admin:content:delete")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        try {
            // 获取信箱
            Article article = articleService.getById(id);
            if (article == null) {
                return Result.error("该文章不存在");
            }

            // 设置删除标志并更新
            article.setIsDel(1);
            boolean updateSuccess = articleService.updateById(article);

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
     * 修改文章状态
     *
     * @param id     文章ID
     * @param status 文章状态
     */
    @ApiOperation(value = "修改文章状态")
    @GetMapping("/status")
    @AutoLog("修改文章状态")
    public Result updateArticleStatus(Integer id, Integer status) {
        Article article = articleService.getById(id);
        if (article == null) {
            return Result.error("文章不存在");
        }
        article.setStatus(status);
        boolean updated = articleService.updateById(article);
        if (updated) {
            return Result.success("文章状态修改成功");
        } else {
            return Result.error("文章状态修改失败");
        }
    }

    /**
     * web查询焦点关注和时政资讯
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "web查询焦点关注和时政资讯")
    @GetMapping("FocusAndCurrentPolitics")
    public Result FocusAndCurrentPolitics(@Validated ArticleSearchRequest request) {
        Page<Article> focusPage = new Page<>(request.getFocusPage(), request.getFocusLimit());
        Page<Article> currentPoliticsPage = new Page<>(request.getCurrentPoliticsPage(), request.getCurrentPoliticsLimit());
        return Result.success(this.articleService.FocusAndCurrentPolitics(focusPage, currentPoliticsPage));
    }

    /**
     * web查询强军资讯、祁连讲习所、祁连论剑阁、工作指导、强军服务
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "web查询强军资讯、祁连讲习所、祁连论剑阁、工作指导、强军服务")
    @GetMapping("StrongMilitaryInformation")
    public Result StrongMilitaryInformation(@Validated @ModelAttribute ArticleSearchRequest request) {
        Page<Article> generalPage = new Page<>(request.getPage(), request.getLimit());
        return Result.success(this.articleService.StrongMilitaryInformation(generalPage));
    }

    /**
     * web查询祁连新动态
     *
     * @return
     */
    @ApiOperation(value = "web查询祁连新动态")
    @GetMapping("NewDevelopments")
    public Result NewDevelopments() {
        return Result.success(this.articleService.NewDevelopments());
    }

    /**
     * 稿件统计
     *
     * @return
     */
    @ApiOperation(value = "稿件统计")
    @GetMapping("categoryCounts")
    public Result<CategoryCountResponse> getCategoryCounts(String startTime, String endTime) {
        return Result.success(articleService.getCategoryCounts(startTime,endTime));
    }

    /**
     * 获取文章详情
     *
     * @param id 文章ID
     * @return 文章详情
     */
    @ApiOperation(value = "获取文章详情")
    @GetMapping("/info")
    public Result getArticleDetail(Integer id) {
        Article article = articleService.getInfo(id);
        if (article == null) {
            return Result.error("文章不存在");
        }
        article.setReleaseTime(article.getCreateTime());
        // 更新文章浏览量
        article.setViewCount(article.getViewCount() != null ? article.getViewCount() + 1 : 1);
        boolean updateSuccess = articleService.updateById(article);
        if (!updateSuccess) {
            return Result.error("更新浏览量失败");
        }

        return Result.success(article);
    }

    /**
     * 审核文章
     *
     * @param id     文章ID
     * @param status 审核状态（例如：1-通过，2-拒绝）
     * @return 审核结果
     */
    @ApiOperation(value = "审核文章")
    @GetMapping("/audit")
    @AutoLog("审核文章")
    public Result auditArticle(Integer id, Integer status) {
        if (!StpUtil.hasPermission("admin:content:examine")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        try {
            // 获取当前登录用户
            Integer loginId = StpUtil.getLoginIdAsInt();
            SysUser sysUser = iSysUserService.getById(loginId);
            if (sysUser == null) {
                return Result.error("用户未登录");
            }

            // 获取文章
            Article article = articleService.getById(id);
            if (article == null) {
                return Result.error("该文章不存在");
            }

            // 更新文章状态
            article.setStatus(status);
            article.setReviewer(sysUser.getUsername());
            boolean updated = articleService.updateById(article);
            if (updated) {
                return Result.success("文章审核成功");
            } else {
                return Result.error("文章审核失败，请稍后再试");
            }
        } catch (Exception e) {
            log.error("Error occurred while auditing article with id: " + id, e);
            return Result.error("系统错误，请稍后再试");
        }
    }

    /**
     * 时政头条轮播图数据来源改为：部队动态 + 时政头条的前五条轮播图
     */
    @ApiOperation(value = "时政头条轮播图数据来源改为：部队动态 + 时政头条的前五条轮播图")
    @GetMapping("currentPoliticsHeadline")
    public Result currentPoliticsHeadline() {
        List<Article> articles = articleService.getCurrentPoliticsHeadline();
        return Result.success(articles);
    }
}
