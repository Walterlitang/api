package com.app.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.app.common.TotalConfig;
import com.app.mapper.SysRoleMapper;
import com.app.model.SysRole;
import com.app.model.SysUser;
import com.app.service.ISysRoleService;
import com.app.service.ISysUserService;
import com.app.util.Result;
import com.app.vo.SysRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author yoominic
 * @since 2023-12-05
 */
@Api(tags = "admin角色接口")
@RestController
@RequestMapping("/adminRole")
public class SysRoleController {
    @Autowired
    private ISysRoleService iSysRoleService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 获取管理员角色
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "获取管理员角色")
    @GetMapping("/getAdminRole")
    public Result getAdminRole() {
        SysUser adminUser = iSysUserService.getById(StpUtil.getLoginIdAsLong());
        if (adminUser == null) {
            return Result.error("用户不存在");
        }
        SysRole adminRole = iSysRoleService.getById(adminUser.getRoleId());
        List<String> roleModelList = new ArrayList<>();
        if (StrUtil.isNotBlank(adminRole.getRules())) {
            String[] roleArr = adminRole.getRules().split(",");//获取字符串
            roleModelList.addAll(Arrays.asList(roleArr));
            adminRole.setPermissionList(roleModelList);
        }
        return Result.success(adminRole);
    }

    /**
     * 按id获取角色
     *
     * @param id id
     * @return {@link Result}
     */
    @ApiOperation(value = "按id获取角色")
    @GetMapping("/getRoleById")
    public Result getRoleById(Integer id) {
        if (!StpUtil.hasPermission("admin:system:role:detail")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        SysUser adminUser = iSysUserService.getById(StpUtil.getLoginIdAsLong());
        if (adminUser == null) {
            return Result.error("用户不存在");
        }
        SysRole adminRole = iSysRoleService.getById(id);
        String[] roleArr = adminRole.getRules().split(",");//获取字符串
        List<String> roleModelList = new ArrayList<>(Arrays.asList(roleArr));
        adminRole.setPermissionList(roleModelList);
        return Result.success(adminRole);
    }

    /**
     * 获取角色列表
     *
     * @param page    第页
     * @param limit   限制
     * @param keyword 关键词
     * @return {@link Result}
     */
    @ApiOperation(value = "获取角色列表")
    @GetMapping("/getRoleList")
    public Result getRoleList(@RequestParam("page") int page,
                              @RequestParam("limit") int limit,
                              @RequestParam(required = false) String keyword) {
        SysUser adminUser = iSysUserService.getById(StpUtil.getLoginIdAsLong());
        if (adminUser == null) {
            return Result.error("用户不存在");
        }
        SysRoleVo roleList = iSysRoleService.getRoleList(page, limit, keyword);
        return Result.success(roleList);
    }

    /**
     * 添加和更新角色
     *
     * @param SysRole sys角色
     * @return {@link Result}
     */
    @ApiOperation(value = "添加和更新角色")
    @PostMapping("/addAndUpdateRole")
    public Result addAndUpdateRole(@RequestBody SysRole SysRole) {
        if (!StpUtil.hasPermission("admin:system:role:save")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        SysUser adminUser = iSysUserService.getById(StpUtil.getLoginIdAsLong());
        if (adminUser == null) {
            return Result.error("用户不存在");
        }
        SysRole adminRole = iSysRoleService.getById(SysRole.getId());
        if (adminRole != null) {
            SysRole.setUpdateTime(LocalDateTime.now());
            iSysRoleService.updateById(SysRole);
            return Result.success("修改成功");
        } else {
            SysRole.setCreateTime(LocalDateTime.now());
            iSysRoleService.save(SysRole);
            return Result.success("添加成功");
        }
    }

    /**
     * 更新角色状态
     *
     * @param id     id
     * @param status 状态
     * @return {@link Result}
     */
    @ApiOperation(value = "更新角色状态")
    @GetMapping("/updateRoleStatus")
    public Result updateRoleStatus(@RequestParam("id") Integer id, @RequestParam("status") Integer status) {
        if (!StpUtil.hasPermission("admin:system:role:status")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        SysUser adminUser = iSysUserService.getById(StpUtil.getLoginIdAsLong());
        if (adminUser == null) {
            return Result.error("用户不存在");
        }
        SysRole roleModel = new SysRole();
        roleModel.setId(id);
        roleModel.setStatus(status);
        iSysRoleService.updateById(roleModel);
        return Result.success("操作成功");
    }

    /**
     * del角色
     *
     * @param id id
     * @return {@link Result}
     */
    @ApiOperation(value = "del角色")
    @GetMapping("/delRole")
    public Result delRole(Integer id) {
        if (!StpUtil.hasPermission("admin:system:role:delete")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        SysUser adminUser = iSysUserService.getById(StpUtil.getLoginIdAsLong());
        if (adminUser == null) {
            return Result.error("用户不存在");
        }
        iSysRoleService.removeById(id);
        return Result.success("删除成功");
    }

    /**
     * 按状态获取角色
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "按状态获取角色")
    @GetMapping("/getRoleByStatus")
    public Result getRoleByStatus() {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        return Result.success(sysRoleMapper.selectList(queryWrapper));
    }
}

