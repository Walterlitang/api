package com.app.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.app.aspect.AutoLog;
import com.app.common.TotalConfig;
import com.app.model.ArticleComment;
import com.app.model.SysUser;
import com.app.service.IArticleCommentService;
import com.app.service.ISysUserService;
import com.app.util.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 文章评论表 前端控制器
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
@Api(tags = "文章评论表")
@RestController
@RequestMapping("/articleComment")
public class ArticleCommentController {

    @Autowired
    private IArticleCommentService articleCommentService;

    @Autowired
    private ISysUserService iSysUserService;

    @ApiOperation("新增评论")
    @PostMapping("/add")
    public Result addComment(@RequestBody ArticleComment comment) {
        try {
            Integer loginId = StpUtil.getLoginIdAsInt();
            SysUser sysUser = iSysUserService.getById(loginId);
            if (sysUser == null) {
                return Result.error("用户未登录");
            }
            // 输入验证
            if (comment == null) {
                return Result.error("评论对象无效");
            }

            boolean savedOrUpdated;
            if (comment.getId() != null && comment.getId() != 0) {
                savedOrUpdated = this.articleCommentService.updateById(comment);
            } else {
                comment.setProfilePicture("/file/默认头像@2x.png");
                comment.setUserName(sysUser.getUsername());
                comment.setCreateTime(LocalDateTime.now());
                savedOrUpdated = this.articleCommentService.save(comment);
            }

            if (savedOrUpdated) {
                return Result.success("评论保存或更新成功");
            } else {
                return Result.error("评论保存或更新失败");
            }
        } catch (Exception e) {
            return Result.error("系统错误，请稍后再试");
        }
    }


    @ApiOperation("删除评论")
    @AutoLog("删除评论")
    @GetMapping("/delete")
    public Result deleteComment(Integer id) {
        if (!StpUtil.hasPermission("admin:content:comment:delete")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        try {
            // 获取轮播图
            ArticleComment comment = this.articleCommentService.getById(id);
            if (comment == null) {
                return Result.error("该评论不存在");
            }

            // 设置删除标志并更新
            comment.setIsDel(1);
            boolean updateSuccess = this.articleCommentService.updateById(comment);

            if (updateSuccess) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败，请稍后再试");
            }
        } catch (Exception e) {
            return Result.error("系统错误，请稍后再试");
        }
    }

    @ApiOperation("管理后台根据文章ID获取评论")
    @GetMapping("/byArticleId")
    public Result getCommentsByArticleId(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size,
                                         @RequestParam(value = "content", required = false) String content,
                                         @RequestParam(value = "articleId") Integer articleId) {
        if (!StpUtil.hasPermission("admin:content:comment")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        Page<ArticleComment> commentPage = new Page<>(page, size);
        return Result.success(this.articleCommentService.byArticleId(commentPage, content, articleId));
    }

    @ApiOperation("web根据文章ID获取评论")
    @GetMapping("/webByArticleId")
    public Result webByArticleId(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size,
                                         @RequestParam(value = "content", required = false) String content,
                                         @RequestParam(value = "articleId") Integer articleId) {
        Page<ArticleComment> commentPage = new Page<>(page, size);
        return Result.success(this.articleCommentService.webByArticleId(commentPage, content, articleId));
    }

    /**
     * 修改文章评论状态
     *
     * @param id     文章ID
     * @param status 文章状态
     */
    @ApiOperation(value = "修改文章评论状态")
    @GetMapping("/status")
    @AutoLog("修改文章评论状态")
    public Result updateArticleStatus(Integer id, @RequestParam Integer status) {
        ArticleComment comment = articleCommentService.getById(id);
        if (comment == null) {
            return Result.error("文章评论不存在");
        }
        comment.setStatus(status);
        boolean updated = articleCommentService.updateById(comment);
        if (updated) {
            return Result.success("文章评论状态修改成功");
        } else {
            return Result.error("文章评论状态修改失败");
        }
    }
}
