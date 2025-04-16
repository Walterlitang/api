package com.app.service;


import com.app.model.SysCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 公用数据配置	 服务类
 * </p>
 *
 * @author yoominic
 * @since 2023-12-05
 */
public interface ISysCategoryService extends IService<SysCategory> {

    List<SysCategory> getCategoryTree(List<SysCategory> catModelList,Integer pid);

    /**
     * init类别树
     */
    public void initCategoryTree();

    public boolean isLastLevelCategory(int id);

    List<SysCategory> getCategoryTreeById(Integer id);

    List<Integer> getChildCategoryIds(Integer cid);
}
