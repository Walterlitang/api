package com.app.service;


import com.app.model.SysPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author yoominic
 * @since 2023-12-05
 */
public interface ISysPermissionService extends IService<SysPermission> {

    List<SysPermission> getMenuList(List<SysPermission> adminPermissionList);
}
