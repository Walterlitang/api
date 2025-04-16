package com.app.controller;


import com.app.model.SysLogs;
import com.app.service.ISysLogsService;
import com.app.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @author yoominic
 * @since 2025-01-22
 */
@RestController
@RequestMapping("/sysLogs")
public class SysLogsController {

    @Autowired
    private ISysLogsService sysLogsService;

    /**
     * 第页
     *
     * @param page  第页
     * @param limit 限制
     * @return {@link Result }
     */
    @GetMapping("page")
    public Result page(@RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        Page<SysLogs> sysLogsPage = new Page<>(page, limit);
        QueryWrapper<SysLogs> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.orderByDesc("created_at");
        IPage<SysLogs> sysLogsIPage = this.sysLogsService.page(sysLogsPage, articleQueryWrapper);
        return Result.success(sysLogsIPage);
    }

}

