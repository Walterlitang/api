package com.app.service;

import com.app.model.AdminUserModel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AdminUserService extends IService<AdminUserModel> {

    AdminUserModel getUserByUserName(String account, String pwd);

    AdminUserModel getUserByUserId(int id);

    Page<AdminUserModel> getPageUserList(int page, int limit, String account);//角色管理分页查询

    void update(AdminUserModel model);

    void insert(AdminUserModel model);

    void delete(int id);

    AdminUserModel getUserByName(String account);

    AdminUserModel login(String username, String md5Password);
}
