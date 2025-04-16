package com.app.service.Impl;

import cn.hutool.json.JSONUtil;
import com.app.common.RedisConstant;
import com.app.mapper.SysCategoryMapper;
import com.app.model.SysCategory;
import com.app.service.ISysCategoryService;
import com.app.service.RedisService;
import com.app.util.FindChildrenUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 公用数据配置	 服务实现类
 * </p>
 *
 * @author yoominic
 * @since 2023-12-05
 */
@Service
public class SysCategoryServiceImpl extends ServiceImpl<SysCategoryMapper, SysCategory> implements ISysCategoryService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysCategoryMapper sysCategoryMapper;

    @Override
    public List<SysCategory> getCategoryTree(List<SysCategory> categoryList, Integer pid) {
        if (pid == null) {
            pid = 0;
        }
        return FindChildrenUtil.findCategoryChildren(categoryList, pid);
    }

    @Override
    public void initCategoryTree() {
        QueryWrapper<SysCategory> sysCategoryQueryWrapper = new QueryWrapper<>();
        sysCategoryQueryWrapper.eq("status", 1);
        sysCategoryQueryWrapper.orderByAsc("sort");
        List<SysCategory> categoryList = this.list(sysCategoryQueryWrapper);
        List<SysCategory> categoryTree = getCategoryTree(categoryList, 0);
        this.redisService.set(RedisConstant.CATEGORY_CACHE_PREFIX, JSONUtil.toJsonStr(categoryTree));
    }

    /**
     * 判断指定的分类ID是否为最后一级分类
     *
     * @param id 分类ID
     * @return 如果是最后一级分类返回true，否则返回false
     */
    public boolean isLastLevelCategory(int id) {
        QueryWrapper<SysCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", id);
        return sysCategoryMapper.selectCount(queryWrapper) == 0;
    }

    @Override
    public List<SysCategory> getCategoryTreeById(Integer id) {
        List<SysCategory> newList = new ArrayList<>();

        try {
            // 获取当前分类
            SysCategory sysCategory = sysCategoryMapper.selectById(id);
            if (sysCategory == null) {
                return newList; // 如果分类不存在，返回空列表
            }

            // 查询子集分类以判断是否为父级分类
            QueryWrapper<SysCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("pid", id);
            queryWrapper.eq("status", 1);
            List<SysCategory> childCategories = sysCategoryMapper.selectList(queryWrapper);

            if (childCategories.isEmpty()) {
                // 如果没有子分类，则查询所有父级分类及其子集分类
                Integer parentId = sysCategory.getPid();
                if (parentId != null && parentId != 0) {
                    addParentAndChildrenCategories(parentId, newList);
                } else {
                    // 如果没有子分类，则直接将当前分类添加到结果列表中
                    newList.add(sysCategory);
                }
            } else {
                // 如果有子分类，则返回该父级分类及其所有子集分类
                newList.add(sysCategory);
                newList.addAll(childCategories);
                for (SysCategory child : childCategories) {
                    addChildrenCategories(child.getId(), newList);
                }
            }

        } catch (Exception e) {
            // 记录日志并返回空列表，或者根据业务需求进行其他处理
            log.error("Error occurred while fetching category tree by id: " + id, e);
            return newList;
        }

        return newList;
    }

    @Override
    public List<Integer> getChildCategoryIds(Integer cid) {
        List<Integer> childCategoryIds = new ArrayList<>();
        List<SysCategory> childCategories = sysCategoryMapper.selectList(new QueryWrapper<SysCategory>().eq("pid", cid));
        for (SysCategory childCategory : childCategories) {
            childCategoryIds.add(childCategory.getId());
            childCategoryIds.addAll(getChildCategoryIds(childCategory.getId()));
        }
        return childCategoryIds;
    }

    private void addParentAndChildrenCategories(Integer parentId, List<SysCategory> newList) {
        SysCategory parentCategory = sysCategoryMapper.selectById(parentId);
        if (parentCategory != null) {
            newList.add(0, parentCategory); // 将父级分类插入到列表开头

            // 查询子集分类
            QueryWrapper<SysCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("pid", parentId);
            List<SysCategory> childCategories = sysCategoryMapper.selectList(queryWrapper);
            newList.addAll(childCategories);

            // 递归查询父级分类
            Integer grandParentId = parentCategory.getPid();
            if (grandParentId != null && grandParentId != 0) {
                addParentAndChildrenCategories(grandParentId, newList);
            }
        }
    }

    private void addChildrenCategories(Integer parentId, List<SysCategory> newList) {
        QueryWrapper<SysCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", parentId);
        List<SysCategory> childCategories = sysCategoryMapper.selectList(queryWrapper);
        newList.addAll(childCategories);

        for (SysCategory child : childCategories) {
            addChildrenCategories(child.getId(), newList);
        }
    }


}
