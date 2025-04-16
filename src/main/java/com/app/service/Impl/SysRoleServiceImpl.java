package com.app.service.Impl;

import com.app.mapper.SysRoleMapper;
import com.app.model.SysRole;
import com.app.service.ISysRoleService;
import com.app.util.StringTool;
import com.app.vo.SysRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author yoominic
 * @since 2023-12-05
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public SysRoleVo getRoleList(int page, int limit, String roleName) {
        LambdaQueryWrapper<SysRole> lqw = new LambdaQueryWrapper<>();
        if (StringTool.isNotEmpty(roleName)) {
            lqw.eq(SysRole::getRoleName, roleName);
        }
        Page<SysRole> page1 = new Page<>(page, limit);
        Page<SysRole> adminRolePage = sysRoleMapper.selectPage(page1, lqw);
        List<SysRole> roleList = adminRolePage.getRecords();
        long count = adminRolePage.getTotal();
        SysRoleVo sysRoleVo = new SysRoleVo();
        sysRoleVo.setList(roleList);
        sysRoleVo.setCount((int) count);
        return sysRoleVo;
    }

    @Override
    public List<SysRole> getRoleByStatus() {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        return sysRoleMapper.selectList(queryWrapper);
    }


    public List<String> getRoleNameByUserId(Integer userId) {
        SysRole sysRole = sysRoleMapper.selectRoleById(userId);
        String rules = sysRole.getRules();
        String[] split = rules.split(";");
        return Arrays.asList(split);
    }
}
