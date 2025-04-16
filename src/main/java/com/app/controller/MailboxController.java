package com.app.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.app.aspect.AutoLog;
import com.app.common.RoleConstants;
import com.app.model.Mailbox;
import com.app.model.SysUser;
import com.app.service.IMailboxService;
import com.app.service.ISysUserService;
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

/**
 * <p>
 * 信箱管理表 前端控制器
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
@Api(tags = "信箱管理")
@RestController
@RequestMapping("/mailbox")
@Slf4j
public class MailboxController {

    @Autowired
    private IMailboxService mailboxService;

    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 新增或编辑信箱
     *
     * @param mailbox 信箱对象
     * @return 操作结果
     */
    @ApiOperation(value = "新增或编辑信件")
    @AutoLog("新增或编辑信件")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Mailbox mailbox) {
        try {
            Integer loginId = StpUtil.getLoginIdAsInt();
            SysUser sysUser = iSysUserService.getById(loginId);
            if (sysUser == null) {
                return Result.error("用户未登录");
            }
            // 输入验证
            if (mailbox == null) {
                return Result.error("信件对象无效");
            }

            boolean savedOrUpdated;
            if (mailbox.getId() != null) {
                savedOrUpdated = mailboxService.updateById(mailbox);
            } else {
                if (mailbox.getUserId() != null) {
                    mailbox.setType(1);
                } else {
                    mailbox.setType(2);
                }
                mailbox.setAddresserId(sysUser.getId());
                mailbox.setProfilePicture("/file/默认头像@2x.png");
                mailbox.setUserName(sysUser.getUsername());
                mailbox.setCreateTime(LocalDateTime.now());
                savedOrUpdated = mailboxService.save(mailbox);
            }

            if (savedOrUpdated) {
                return Result.success("信件保存或更新成功");
            } else {
                return Result.error("信件保存或更新失败");
            }
        } catch (Exception e) {
            return Result.error("系统错误，请稍后再试");
        }
    }

    /**
     * 删除信箱
     *
     * @param id 信箱ID
     * @return 操作结果
     */
    @ApiOperation(value = "删除信件")
    @AutoLog("删除信件")
    @GetMapping("/delete")
    public Result delete(Integer id) {
        try {
            // 获取信箱
            Mailbox mailbox = mailboxService.getById(id);
            if (mailbox == null) {
                return Result.error("该信件不存在");
            }

            // 设置删除标志并更新
            mailbox.setIsDel(1);
            boolean updateSuccess = mailboxService.updateById(mailbox);

            if (updateSuccess) {
                return Result.success("信件删除成功");
            } else {
                return Result.error("信件删除失败，请稍后再试");
            }
        } catch (Exception e) {
            return Result.error("系统错误，请稍后再试");
        }
    }

    /**
     * 根据ID查询信箱
     *
     * @param id 信箱ID
     * @return 信箱对象
     */
    @ApiOperation(value = "查询信件详情")
    @GetMapping("/info")
    public Result info(Integer id) {
        return Result.success(mailboxService.getById(id));
    }

    /**
     * 管理后台查询信箱列表
     *
     * @return 信箱列表
     */
    @ApiOperation(value = "管理后台查询信件列表")
    @GetMapping("/list")
    public Result page(
            @RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "isReply", required = false) Integer isReply) {
        try {
            Page<Mailbox> mailboxPage = new Page<>(page, size);
            Integer loginId = StpUtil.getLoginIdAsInt();
            SysUser sysUser = iSysUserService.getById(loginId);

            if (sysUser == null) {
                log.warn("用户未登录，loginId: {}", loginId);
                return Result.error("用户未登录");
            }

            Integer userId;
            switch (sysUser.getRoleId()) {
                case RoleConstants.CHIEF_ROLE_ID:
                    userId = loginId;
                    break;
                case RoleConstants.DISCIPLINE_INSPECTION_ROLE_ID:
                    userId = 0;
                    break;
                case RoleConstants.ADMIN_ROLE_ID:
                    userId = null;
                    break;
                default:
                    log.warn("用户权限不足，roleId: {}", sysUser.getRoleId());
                    return Result.error("权限不足");
            }

            IPage<Mailbox> mailboxIPage = mailboxService.adminPage(mailboxPage, title, userId, type, isReply, categoryId);
            log.info("查询信箱列表成功，page: {}, size: {}, title: {}", page, size, title);
            return Result.success(mailboxIPage);
        } catch (Exception e) {
            log.error("查询信箱列表失败", e);
            return Result.error("系统错误");
        }
    }


    /**
     * 回复信箱并修改状态
     *
     * @param id           信箱ID
     * @param replyContent 回复内容
     * @return 操作结果
     */
    @ApiOperation(value = "回复信件并修改状态")
    @AutoLog("回复信件并修改状态")
    @PostMapping("/reply")
    public Result reply(Integer id, @RequestParam String replyContent) {
        try {
            // 获取信箱
            Mailbox mailbox = mailboxService.getById(id);
            if (mailbox == null) {
                return Result.error("该信件不存在");
            }
            Integer loginId = StpUtil.getLoginIdAsInt();
            if (loginId != 1) {
                if (mailbox.getUserId() != 0) {
                    if (!mailbox.getUserId().equals(loginId)) {
                        return Result.error("权限不足");
                    }
                } else {
                    SysUser sysUser = iSysUserService.getById(loginId);
                    if (sysUser == null) {
                        return Result.error("用户未登录");
                    }
                    if (sysUser.getRoleId() != RoleConstants.DISCIPLINE_INSPECTION_ROLE_ID) {
                        return Result.error("权限不足");
                    }
                }
            }
            // 设置回复内容和状态
            mailbox.setReplyContent(replyContent);
            mailbox.setIsReply(1);
            mailbox.setReplyTime(LocalDateTime.now());

            boolean updateSuccess = mailboxService.updateById(mailbox);

            if (updateSuccess) {
                return Result.success("回复成功");
            } else {
                return Result.error("回复失败，请稍后再试");
            }
        } catch (Exception e) {
            return Result.error("系统错误，请稍后再试");
        }
    }

    /**
     * web查询信箱列表
     *
     * @return 信箱列表
     */
    @ApiOperation(value = "web查询信箱列表")
    @GetMapping("/webList")
    public Result webList(
            @RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "isReply", required = false) Integer isReply,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "categoryId", required = false) Integer categoryId) {
        try {
            Page<Mailbox> mailboxPage = new Page<>(page, size);
            IPage<Mailbox> mailboxIPage = mailboxService.adminPage(mailboxPage, title, null, type, isReply, categoryId);
            log.info("查询信箱列表成功，page: {}, size: {}, title: {}", page, size, title);
            return Result.success(mailboxIPage);
        } catch (Exception e) {
            log.error("查询信箱列表失败", e);
            return Result.error("系统错误");
        }
    }
}
