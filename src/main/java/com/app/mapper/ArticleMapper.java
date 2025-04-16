package com.app.mapper;

import com.app.ResponseVo.CategoryCountResponse;
import com.app.model.Article;
import com.app.model.SysCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文章内容表 Mapper 接口
 * </p>
 *
 * @author yoominic
 * @since 2025-01-21
 */
public interface ArticleMapper extends BaseMapper<Article> {

    IPage<Article> webPage(Page<Article> articlePage,
                           @Param("title") String title,
                           @Param("cid") Integer cid,
                           @Param("startTime") String startTime,
                           @Param("endTime") String endTime);

    IPage<Article> adminPage(Page<Article> articlePage,
                             @Param("title") String title,
                             @Param("departmentId") Integer departmentId,
                             @Param("cid") Integer cid,
                             @Param("startTime") String startTime,
                             @Param("endTime") String endTime,
                             @Param("userId") Integer userId,
                             @Param("roleId") Integer roleId,
                             @Param("status") Integer status);


    IPage<Article> FocusPage(Page<Article> articlePage, @Param("categoryId") Integer categoryId);

    IPage<Article> CurrentPoliticsPage(Page<Article> currentPoliticsPage, @Param("categoryId") Integer categoryId);

    IPage<Article> getPageByCategoryId(Page<Article> page,
                                       @Param("categoryId") Integer categoryId);

    List<SysCategory> getSubCategoriesByParentId(@Param("parentId") Integer parentId);

    IPage<Article> getRecentArticlesByCategoryId(Page<Article> page,
                                                 @Param("categoryId") Integer categoryId);

    List<CategoryCountResponse> getCategoryCounts(@Param("parentId") Integer parentId, @Param("startTime") String startTime, @Param("endTime") String endTime);


    IPage<Article> webPageByCategoryIds(Page<Article> articlePage,
                                        @Param("title") String title,
                                        @Param("childCategoryIds") List<Integer> childCategoryIds,
                                        @Param("startTime") String startTime,
                                        @Param("endTime") String endTime);

    Article getInfo(@Param("id") Integer id);

    List<Article> getCurrentPoliticsHeadline();

}
