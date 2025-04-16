package com.app.service.Impl;

import com.app.mapper.AdminUserMapper;
import com.app.model.AdminUserModel;
import com.app.service.AdminUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


@Service
@Primary
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUserModel> implements AdminUserService {
    @Autowired
    AdminUserMapper adminUserMapper;

    @Override
    public AdminUserModel getUserByUserName(String account, String pwd) {
        QueryWrapper<AdminUserModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.select().eq("account", account).eq("pwd", pwd);
        AdminUserModel user = adminUserMapper.selectOne(queryWrapper);
        if (user == null) {
            return null;
        } else {
            return user;
        }
    }

    @Override
    public AdminUserModel getUserByUserId(int id) {
        return adminUserMapper.selectById(id);
    }

    @Override
    public Page<AdminUserModel> getPageUserList(int page, int limit, String account) {
        QueryWrapper<AdminUserModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.select();
        if (account != null) {
            queryWrapper.like("account", account);
        }
        queryWrapper.orderByAsc("id");
        Page<AdminUserModel> pages = new Page<>();
        pages.setCurrent(page);
        pages.setSize(limit);
        Page<AdminUserModel> userList = adminUserMapper.selectPage(pages, queryWrapper);
        return userList;
    }


    @Override
    public void update(AdminUserModel model) {
        adminUserMapper.updateById(model);
    }

    @Override
    public void insert(AdminUserModel model) {
        adminUserMapper.insert(model);
    }

    @Override
    public void delete(int id) {
        adminUserMapper.deleteById(id);
    }

    @Override
    public AdminUserModel getUserByName(String account) {
        QueryWrapper<AdminUserModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.select();
        queryWrapper.eq("account", account);
        AdminUserModel user = adminUserMapper.selectOne(queryWrapper);
        return user;
    }

    @Override
    public AdminUserModel login(String username, String md5Password) {
        LambdaQueryWrapper<AdminUserModel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminUserModel::getAccount, username);
        queryWrapper.eq(AdminUserModel::getPwd, md5Password);
        return adminUserMapper.selectOne(queryWrapper);
    }

}
