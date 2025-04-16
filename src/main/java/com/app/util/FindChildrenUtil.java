package com.app.util;

import com.app.model.SysCategory;
import com.app.model.SysPermission;

import java.util.ArrayList;
import java.util.List;

public class FindChildrenUtil {

    //查询权限配置树
    public static List<SysPermission> findChildren(List<SysPermission> modelList, Integer pid) {
        List<SysPermission> personModelList = new ArrayList<>();
        for (int i = 0; i < modelList.size(); i++) {
            if (modelList.get(i).getPid().equals(pid)) {
                SysPermission model = modelList.get(i);
                List<SysPermission> modelList1 = findChildren(modelList, model.getId());
                for (int j = 0; j < modelList1.size(); j++) {
                    model.setChildList(modelList1);
                }
                personModelList.add(model);
            }
        }
        return personModelList;
    }

    //查询公用配置数据树
    public static List<SysCategory> findCategoryChildren(List<SysCategory> modelList, Integer pid) {
        List<SysCategory> personModelList = new ArrayList<>();
        for (int i = 0; i < modelList.size(); i++) {
            if (modelList.get(i).getPid().equals(pid)) {
                SysCategory model = modelList.get(i);
                List<SysCategory> modelList1 = findCategoryChildren(modelList, model.getId());
                for (int j = 0; j < modelList1.size(); j++) {
                    model.setChild(modelList1);
                }
                personModelList.add(model);
            }
        }
        return personModelList;
    }

    public static List<SysPermission> findChildren2(List<SysPermission> modelList, Integer pid) {
        List<SysPermission> personModelList = new ArrayList<>();
        for (int i = 0; i < modelList.size(); i++) {
            if (modelList.get(i).getPid().equals(pid)) {
                SysPermission model = modelList.get(i);
                List<SysPermission> modelList1 = findChildren(modelList, model.getId());
                for (int j = 0; j < modelList1.size(); j++) {
                    model.setChildList(modelList1);
                }
                personModelList.add(model);
            }
        }
        return personModelList;
    }
}
