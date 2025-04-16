package com.app.service.Impl;

import com.app.mapper.PermissionMapper;
import com.app.model.PermissionModel;
import com.app.service.PermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class PermissionServiceImpl implements PermissionService {


    @Autowired
    PermissionMapper permissionMapper;

    @Override
    public PermissionModel selectById(int id) {
        return permissionMapper.selectById(id);
    }

    @Override
    public List<PermissionModel> getModelByIds(List<Integer> ids) {
        QueryWrapper<PermissionModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        return permissionMapper.selectList(queryWrapper);
    }

    @Override
    public List<PermissionModel> selectTree() {
        QueryWrapper<PermissionModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.select().orderByAsc("sort");
        return permissionMapper.selectList(queryWrapper);
    }

    @Override
    public List<PermissionModel> getListByNotMenuType(String type) {
        QueryWrapper<PermissionModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("menu_type", type).orderByAsc("sort").orderByDesc("id");
        return permissionMapper.selectList(queryWrapper);
    }
}
