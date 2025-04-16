package com.app.service.Impl;

import com.app.mapper.RoleMapper;
import com.app.model.RoleModel;
import com.app.service.RoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


@Service
@Primary
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleMapper roleMapper;

    @Override
    public RoleModel getRoleByRoleId(int id) {
        QueryWrapper<RoleModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("rules", "role_name", "status", "id").eq("id", id);
        return roleMapper.selectOne(queryWrapper);
    }

    @Override
    public Page<RoleModel> getPageRoleList(int page, int limit, String roleName, Integer status) {
        QueryWrapper<RoleModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "role_name", "status", "create_time", "update_time");
        if (roleName != null) {
            queryWrapper.like("role_name", roleName);
        }
        if (status != null && status > 0) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByAsc("id");
        Page<RoleModel> pages = new Page<>();
        pages.setCurrent(page);
        pages.setSize(limit);
        return roleMapper.selectPage(pages, queryWrapper);
    }

    @Override
    public void update(RoleModel model) {
        roleMapper.updateById(model);
    }

    @Override
    public void insert(RoleModel model) {
        roleMapper.insert(model);
    }

    @Override
    public void delete(int id) {
        roleMapper.deleteById(id);
    }

    @Override
    public RoleModel getRoleByName(String roleName) {
        QueryWrapper<RoleModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.select().eq("role_name", roleName);
        return roleMapper.selectOne(queryWrapper);
    }
}
