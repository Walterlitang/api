package com.app.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.app.common.TotalConfig;
import com.app.model.SysPermission;
import com.app.model.SysRole;
import com.app.model.SysUser;
import com.app.service.ISysPermissionService;
import com.app.service.ISysRoleService;
import com.app.service.ISysUserService;
import com.app.util.Result;
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
 * 权限 前端控制器
 * </p>
 *
 * @author yoominic
 * @since 2023-12-05
 */
@Api(tags = "admin权限接口")
@RestController
@RequestMapping("/adminPermission")
public class SysPermissionController {
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private ISysPermissionService iSysPermissionService;

    @ApiOperation(value = "获取菜单列表")
    @GetMapping("/getMenuList")
    public Result getMenuList() {
        SysUser adminUser = iSysUserService.getById(StpUtil.getLoginIdAsInt());
        if (adminUser == null) {
            return Result.error("用户不存在");
        }
        SysRole roleModel = iSysRoleService.getById(adminUser.getRoleId());
        QueryWrapper<SysPermission> qw = new QueryWrapper<>();
        qw.in("menu_type", "M", "C");
        qw.orderByAsc("sort");
        List<SysPermission> adminPermissionList = iSysPermissionService.list(qw);//查MC
        if (adminPermissionList == null) {
            return Result.error("暂无数据");
        }
        List<SysPermission> menuList = iSysPermissionService.getMenuList(adminPermissionList);
        List<SysPermission> modelList = new ArrayList<>();
        for (SysPermission adminPermissionModel : menuList) {
            List<SysPermission> childList = adminPermissionModel.getChildList();
            List<SysPermission> childList1 = new ArrayList<>();
            if (childList != null) {
                for (SysPermission permissionModel : childList) {
                    if (roleModel.getRules().contains(permissionModel.getPerms())) {
                        childList1.add(permissionModel);
                    }
                }
                adminPermissionModel.setChildList(childList1);
            }
            if (adminPermissionModel.getPid() == 0) {
                if (roleModel.getRules().contains(adminPermissionModel.getPerms())) {
                    modelList.add(adminPermissionModel);
                }
            }
        }
        return Result.success(modelList);
    }

    //获取(菜单列表或权限管理树) 查全部
    @ApiOperation(value = "获取(菜单列表或权限管理树) 查全部")
    @GetMapping("/getMenuTree")
    public Result getMenuTree(Integer roleId) {
        SysUser adminUser = iSysUserService.getById(StpUtil.getLoginIdAsLong());
        if (adminUser == null) {
            return Result.error("用户不存在");
        }
        QueryWrapper<SysPermission> qw = new QueryWrapper<>();
        qw.orderByAsc("sort");
        List<SysPermission> adminPermissionList = iSysPermissionService.list(qw);//全查
        if (adminPermissionList == null) {
            return Result.error("暂无数据");
        }
        List<SysPermission> menuList = iSysPermissionService.getMenuList(adminPermissionList);
        if (roleId != null) {
            SysRole adminRole = iSysRoleService.getById(roleId);
            String rules = adminRole.getRules();
            List<String> roleModelList = new ArrayList<>();
            if (rules != null && !rules.equals("")) {
                String[] roleArr = rules.split(",");
                roleModelList.addAll(Arrays.asList(roleArr));
            }
            for (String roleModel : roleModelList) {
                for (SysPermission menuAll : adminPermissionList) {
                    String perms = menuAll.getPerms();
                    if (perms.equals(roleModel)) {
                        menuAll.setChecked(true);
                    }
                }
            }
        }
        return Result.success(menuList);
    }

    @ApiOperation(value = "根据权限表id获得对应的权限")
    @GetMapping("/getPermissionById")
    public Result getPermissionById(Integer id) {
        if (!StpUtil.hasPermission("admin:system:menu:detail")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        SysUser adminUser = iSysUserService.getById(StpUtil.getLoginIdAsLong());
        if (adminUser == null) {
            return Result.error("用户不存在");
        }
        SysPermission permission = iSysPermissionService.getById(id);
        return Result.success(permission);
    }


    @ApiOperation(value = "新增和修改菜单")
    @PostMapping("/addAndUpdatePermission")
    public Result addAndUpdatePermission(@RequestBody SysPermission permissionModel) {
        if (!StpUtil.hasPermission("admin:system:menu:save")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        SysUser adminUser = iSysUserService.getById(StpUtil.getLoginIdAsLong());
        if (adminUser == null) {
            return Result.error("用户不存在");
        }
        SysPermission permission = iSysPermissionService.getById(permissionModel.getId());
        if (permission != null) {
            permissionModel.setUpdateTime(LocalDateTime.now());
            iSysPermissionService.updateById(permissionModel);
            return Result.success("修改成功");
        } else {
            permissionModel.setCreateTime(LocalDateTime.now());
            if (permissionModel.getPid() == null) {
                permissionModel.setPid(0);
            }
            iSysPermissionService.save(permissionModel);
            return Result.success("添加成功");
        }
    }

    @ApiOperation(value = "删除菜单")
    @GetMapping("/delPermission")
    public Result delPermission(Integer id) {
        if (!StpUtil.hasPermission("admin:system:menu:delete")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        SysUser adminUser = iSysUserService.getById(StpUtil.getLoginIdAsLong());
        if (adminUser == null) {
            return Result.error("用户不存在");
        }
        iSysPermissionService.removeById(id);
        return Result.success("删除成功");
    }
}

