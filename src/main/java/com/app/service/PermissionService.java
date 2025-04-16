package com.app.service;

import com.app.model.PermissionModel;

import java.util.List;

public interface PermissionService {

    List<PermissionModel> selectTree();

    List<PermissionModel> getListByNotMenuType(String type);

    PermissionModel selectById(int id);

    List<PermissionModel> getModelByIds(List<Integer> ids);
}
