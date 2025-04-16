package com.app.service;

import com.app.model.SysRole;
import com.app.vo.SysRoleVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author yoominic
 * @since 2023-12-05
 */
public interface ISysRoleService extends IService<SysRole> {

    SysRoleVo getRoleList(int page, int limit, String roleName);

    List<SysRole> getRoleByStatus();

}
