package com.app.service.Impl;

import com.app.ResponseVo.ArticleResponse;
import com.app.ResponseVo.CategoryCountResponse;
import com.app.common.ArticleCategory;
import com.app.common.ArticleConstants;
import com.app.mapper.ArticleMapper;
import com.app.model.Article;
import com.app.model.SysCategory;
import com.app.model.SysUser;
import com.app.service.IArticleService;
import com.app.service.ISysCategoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 文章内容表 服务实现类
 * </p>
 *
 * @author yoominic
 * @since 2025-01-21
 */
@Slf4j
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    @Autowired
    private ISysCategoryService iSysCategoryService;

    @Override
    public IPage<Article> webPage(Page<Article> articlePage, String title, Integer cid, String startTime, String endTime) {
//        SysCategory category = iSysCategoryService.getById(cid);
        // 当分类为顶级分类则查询该分类下的所有子集分类的文章
       /* if (category != null && category.getPid() == 0) {
            // 获取所有子分类的ID
            List<Integer> childCategoryIds = iSysCategoryService.getChildCategoryIds(cid);
            // 如果子分类列表不为空，则查询这些子分类的文章
            if (!childCategoryIds.isEmpty()) {
                return this.baseMapper.webPageByCategoryIds(articlePage, title, childCategoryIds, startTime, endTime);
            }
        }*/
        // 否则查询指定分类的文章
        return this.baseMapper.webPage(articlePage, title, cid, startTime, endTime);
    }


    @Override
    public IPage<Article> adminPage(Page<Article> articlePage, String title, Integer cid,
                                    String startTime, String endTime, SysUser sysUser,
                                    Integer departmentId, Integer status) {
        return this.baseMapper.adminPage(articlePage, title, departmentId, cid, startTime, endTime, sysUser.getId(), sysUser.getRoleId(),status);
    }

    @Override
    public ArticleResponse FocusAndCurrentPolitics(Page<Article> focusPage, Page<Article> currentPoliticsPage) {
        IPage<Article> focusPages = this.baseMapper.FocusPage(focusPage, ArticleConstants.FocusID);
        IPage<Article> currentPoliticsPages = this.baseMapper.CurrentPoliticsPage(currentPoliticsPage, ArticleConstants.CurrentPoliticsID);
        ArticleResponse articleResponse = new ArticleResponse();
        articleResponse.setFocusPages(focusPages);
        articleResponse.setCurrentPoliticsPages(currentPoliticsPages);
        return articleResponse;
    }


    @Override
    public ArticleResponse StrongMilitaryInformation(Page<Article> generalPage) {
        if (generalPage == null) {
            log.warn("Input generalPage is null, returning empty ArticleResponse");
            return new ArticleResponse();
        }

        ArticleResponse articleResponse = new ArticleResponse();

        try {
            // 使用 Collections.unmodifiableMap 创建不可变的分页参数配置
            Map<ArticleCategory, Long> pageSizeConfig = Collections.unmodifiableMap(new HashMap<ArticleCategory, Long>() {{
                put(ArticleCategory.ROCKET, 3L);
                put(ArticleCategory.JUN_QING_ZONG_HENG, 4L);
                put(ArticleCategory.THEORY, 3L);
            }});

            for (ArticleCategory category : ArticleCategory.values()) {
                Page<Article> categoryPage = new Page<>(1, pageSizeConfig.getOrDefault(category, generalPage.getSize()));
                IPage<Article> page = this.baseMapper.getPageByCategoryId(categoryPage, category.getCategoryId());
                IPage<Article> newPage = copyPage(page);
                category.getSetterMethod().accept(articleResponse, newPage);
            }
        } catch (Exception e) {
            // 记录日志并抛出自定义异常
            log.error("Error setting pages in StrongMilitaryInformation with input: {}", generalPage, e);
            return new ArticleResponse();
        }

        return articleResponse;
    }

    private IPage<Article> copyPage(IPage<Article> originalPage) {
        IPage<Article> newPage = new Page<>(originalPage.getCurrent(), originalPage.getSize(), originalPage.getTotal());
        newPage.setRecords(new ArrayList<>(originalPage.getRecords()));
        return newPage;
    }


    @Override
    public List<SysCategory> NewDevelopments() {
        // 查询祁连新动态的所有子集
        return this.baseMapper.getSubCategoriesByParentId(ArticleConstants.NewDevelopmentsID);
    }

    @Override
    public List<CategoryCountResponse> getCategoryCounts(String startTime, String endTime) {
        return this.baseMapper.getCategoryCounts(ArticleConstants.NewDevelopmentsID, startTime, endTime);
    }

    @Override
    public Article getInfo(Integer id) {
        return this.baseMapper.getInfo(id);
    }

    @Override
    public List<Article> getCurrentPoliticsHeadline() {
        return this.baseMapper.getCurrentPoliticsHeadline();
    }
}
