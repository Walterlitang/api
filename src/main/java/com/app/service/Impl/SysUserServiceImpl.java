package com.app.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.app.mapper.SysUserMapper;
import com.app.model.SysCategory;
import com.app.model.SysRole;
import com.app.model.SysUser;
import com.app.service.ISysRoleService;
import com.app.service.ISysUserService;
import com.app.vo.SysUserVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yoominic
 * @since 2023-12-05
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysRoleService iSysRoleService;

    @Override
    public SysUser login(String username, String md5Password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        queryWrapper.eq(SysUser::getPassword, md5Password);
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public SysUserVo getAdminList(Integer page, Integer limit, String keyword) {
        Page<SysUser> page1 = new Page<>(page, limit);
        QueryWrapper<SysUser> qw = new QueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            qw.like("username", keyword);
            qw.or().like("real_name", keyword);
        }
        Page<SysUser> adminUserPage = sysUserMapper.selectPage(page1, qw);
        List<SysUser> adminUserList = adminUserPage.getRecords();
        for (SysUser adminUserModel : adminUserList) {
            if (adminUserModel.getRoleId() != null) {
                SysRole byId = iSysRoleService.getById(adminUserModel.getRoleId());
                adminUserModel.setRoleName(byId.getRoleName());
            }
            adminUserModel.setPassword(null);
        }
        long count = adminUserPage.getTotal();
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setList(adminUserList);
        sysUserVo.setCount((int) count);
        return sysUserVo;
    }

    @Override
    public IPage<SysUser> getChiefList(Page<SysUser> sysUserPage) {
        return this.baseMapper.getChiefList(sysUserPage);
    }

    @Override
    public List<SysCategory> getUnitList() {
        return this.baseMapper.getUnitList();
    }
}
