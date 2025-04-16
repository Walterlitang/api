package com.app.service;

import com.app.model.RoleModel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface RoleService {

    RoleModel getRoleByRoleId(int id);

    Page<RoleModel> getPageRoleList(int page, int limit, String roleName, Integer status);//角色管理分页查询

    void update(RoleModel model);

    void insert(RoleModel model);

    void delete(int id);

    RoleModel getRoleByName(String roleName);

}
