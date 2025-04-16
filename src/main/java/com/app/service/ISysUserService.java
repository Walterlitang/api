package com.app.service;


import com.app.model.SysCategory;
import com.app.model.SysUser;
import com.app.vo.SysUserVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yoominic
 * @since 2023-12-05
 */
public interface ISysUserService extends IService<SysUser> {

    SysUser login(String username, String md5Password);

    SysUserVo getAdminList(Integer page, Integer limit, String keyword);

    IPage<SysUser> getChiefList(Page<SysUser> sysUserPage);

    List<SysCategory> getUnitList();

}
