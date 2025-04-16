package com.app.service.Impl;

import com.app.mapper.SysPermissionMapper;
import com.app.model.SysPermission;
import com.app.service.ISysPermissionService;
import com.app.util.FindChildrenUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author yoominic
 * @since 2023-12-05
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {
    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public List<SysPermission> getMenuList(List<SysPermission> adminPermissionList) {
        List<Integer> arr1 = new ArrayList<>();
        for (int i = 0; i < adminPermissionList.size(); i++) {
            arr1.add(adminPermissionList.get(i).getId());
        }
        Integer ArrId = 1;
        boolean result1 = arr1.contains(ArrId);
        List<Integer> pidArr1 = new ArrayList<>();
        if (!result1) {
            for (SysPermission adminPermission : adminPermissionList) {
                boolean pidResult = arr1.contains(adminPermission.getPid());
                if (!pidResult) {
                    pidArr1.add(adminPermission.getPid());
                }
            }
            pidArr1.add(ArrId);
            arr1.addAll(pidArr1);
            LinkedHashSet<Integer> hashSet = new LinkedHashSet<>(arr1);
            ArrayList<Integer> listWithoutDuplicates = new ArrayList<>(hashSet);
            List<SysPermission> SysPermissions1 = new ArrayList<>();
            for (Integer listWithoutDuplicate : listWithoutDuplicates) {
                SysPermission SysPermission = sysPermissionMapper.selectById(listWithoutDuplicate);
                SysPermissions1.add(SysPermission);
            }
            for (int i = 0; i < listWithoutDuplicates.size(); i++) {
                SysPermission SysPermission = sysPermissionMapper.selectById(listWithoutDuplicates);
                SysPermissions1.add(SysPermission);
            }
            List<SysPermission> children = FindChildrenUtil.findChildren(SysPermissions1, 0);
            return children;
        }
        List<SysPermission> childrenList = FindChildrenUtil.findChildren(adminPermissionList, 0);
        return childrenList;
    }
}
