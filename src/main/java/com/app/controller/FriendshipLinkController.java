package com.app.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.app.ResponseVo.ArtResponse;
import com.app.aspect.AutoLog;
import com.app.common.TotalConfig;
import com.app.model.FriendshipLink;
import com.app.model.SysCategory;
import com.app.service.IFriendshipLinkService;
import com.app.service.ISysCategoryService;
import com.app.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 友情链接表 前端控制器
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
@Api(tags = "友情链接")
@RestController
@RequestMapping("/friendshipLink")
@Slf4j
public class FriendshipLinkController {

    @Autowired
    private IFriendshipLinkService friendshipLinkService;
    @Autowired
    private ISysCategoryService sysCategoryService;

    /**
     * 新增或编辑友情链接
     *
     * @param friendshipLink 友情链接对象
     * @return 操作结果
     */
    @ApiOperation(value = "新增或编辑友情链接")
    @AutoLog("新增或编辑友情链接")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody FriendshipLink friendshipLink) {
        if (!StpUtil.hasPermission("admin:friendshipLink:save")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        try {
            // 输入验证
            if (friendshipLink == null) {
                return Result.error("友情链接对象无效");
            }
            if (!sysCategoryService.isLastLevelCategory(friendshipLink.getCId())) {
                return Result.error("请选择正确的分类");
            }

            // 校验 linkAddress 是否以 http:// 或 https:// 开头
            String linkAddress = friendshipLink.getLinkAddress();
            if (StrUtil.isNotBlank(linkAddress)) {
                if (!(linkAddress.startsWith("http://") || linkAddress.startsWith("https://"))) {
                    return Result.error("链接地址必须以 http:// 或 https:// 开头");
                }
            }
            boolean savedOrUpdated;
            if (friendshipLink.getId() != null) {
                savedOrUpdated = friendshipLinkService.updateById(friendshipLink);
            } else {
                //校验该分类下是否存在友情链接
//                if (friendshipLinkService.isExistLinkAddress(friendshipLink.getCId())) {
//                    return Result.error("该分类下链接地址已存在");
//                }
                friendshipLink.setCreateTime(LocalDateTime.now());
                savedOrUpdated = friendshipLinkService.save(friendshipLink);
            }

            if (savedOrUpdated) {
                return Result.success("保存或更新成功");
            } else {
                return Result.error("保存或更新失败");
            }
        } catch (Exception e) {
            return Result.error("系统错误，请稍后再试");
        }
    }

    /**
     * 删除友情链接
     *
     * @param id 友情链接ID
     * @return 操作结果
     */
    @ApiOperation(value = "删除友情链接")
    @AutoLog("删除友情链接")
    @GetMapping("/delete")
    public Result delete(Long id) {
        if (!StpUtil.hasPermission("admin:friendshipLink:delete")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        try {
            // 获取友情链接
            FriendshipLink friendshipLink = friendshipLinkService.getById(id);
            if (friendshipLink == null) {
                return Result.error("该友情链接不存在");
            }

            // 设置删除标志并更新
            friendshipLink.setIsDel(1);
            boolean updateSuccess = friendshipLinkService.updateById(friendshipLink);
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
     * 根据ID查询友情链接
     *
     * @param id 友情链接ID
     * @return 友情链接对象
     */
    @ApiOperation(value = "根据ID查询友情链接")
    @GetMapping("/info")
    public Result info(Long id) {
        FriendshipLink friendshipLink = friendshipLinkService.getById(id);
        if (friendshipLink == null) {
            return Result.error("该友情链接不存在");
        }
        return Result.success(friendshipLink);
    }

    /**
     * 查询所有友情链接
     *
     * @return 友情链接列表
     */
    @ApiOperation(value = "查询所有友情链接")
    @GetMapping("/list")
    public Result list(
            @RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size,
            @RequestParam(value = "cid", required = false) Integer cid,
            @RequestParam(value = "linkAddress", required = false) String linkAddress) {
        if (!StpUtil.hasPermission("admin:friendship-link:list")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        try {
            Page<FriendshipLink> mailboxPage = new Page<>(page, size);
            IPage<FriendshipLink> friendshipLinks = friendshipLinkService.adminPage(mailboxPage, linkAddress, cid);
            return Result.success(friendshipLinks);
        } catch (Exception e) {
            log.error("查询友情链接列表失败", e);
            return Result.error("系统错误");
        }
    }

    /**
     * 修改友情链接状态
     *
     * @param id     友情链接ID
     * @param status 友情链接状态
     */
    @ApiOperation(value = "修改友情链接状态")
    @AutoLog("修改友情链接状态")
    @GetMapping("/status")
    public Result updateArticleStatus(Integer id, Integer status) {
        FriendshipLink friendshipLink = this.friendshipLinkService.getById(id);
        if (friendshipLink == null) {
            return Result.error("友情链接不存在");
        }
        friendshipLink.setStatus(status);
        boolean updated = this.friendshipLinkService.updateById(friendshipLink);
        if (updated) {
            return Result.success("友情链接状态修改成功");
        } else {
            return Result.error("友情链接状态修改失败");
        }
    }


    /**
     * web查询站点导航
     * type
     * 1热点专题
     * 2办事大厅
     * 3站点导航
     * 4祁连艺苑
     *
     * @return web查询站点导航
     */
    @ApiOperation(value = "web查询所有友情链接")
    @GetMapping("/webList")
    public Result webList(Integer type) {
        try {
            List<SysCategory> friendshipLinks = friendshipLinkService.webList(type);
            return Result.success(friendshipLinks);
        } catch (Exception e) {
            log.error("查询友情链接列表失败", e);
            return Result.error("系统错误");
        }
    }

    @ApiOperation(value = "web查询祁连艺苑")
    @GetMapping("/webArtList")
    public Result webArtList() {
        try {
            ArtResponse artResponse = friendshipLinkService.webArtList();
            return Result.success(artResponse);
        } catch (Exception e) {
            log.error("查询祁连艺苑列表失败", e);
            return Result.error("系统错误");
        }
    }
}
