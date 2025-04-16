package com.app.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.app.common.RedisConstant;
import com.app.common.TotalConfig;
import com.app.model.FriendshipLink;
import com.app.model.SysCategory;
import com.app.model.SysUser;
import com.app.service.IFriendshipLinkService;
import com.app.service.ISysCategoryService;
import com.app.service.ISysUserService;
import com.app.service.RedisService;
import com.app.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * <p>
 * 公用数据配置	 前端控制器
 * </p>
 *
 * @author yoominic
 * @since 2023-12-05
 */
@Slf4j
@Api(tags = "admin公用数据配置接口")
@RestController
@RequestMapping("/sysCategory")
public class SysCategoryController {

    @Autowired
    private ISysCategoryService categoryService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IFriendshipLinkService linkService;

    @Autowired
    private ISysUserService iSysUserService;


    /**
     * 获取内容管理的分类树形列表
     */
    @ApiOperation(value = "管理后台获取内容管理的分类树形列表")
    @GetMapping("/getContentManagementTree")
    public Result getContentManagementTree(@RequestParam(value = "pid", required = false) Integer pid, @RequestParam(value = "keyword", required = false) String keyword) {
        return getResult(keyword, "CONTENTMANAGEMENT", null, pid, null);
    }

    /**
     * web获取文章的分类树形列表
     */
    @ApiOperation(value = "web获取文章的分类树形列表")
    @GetMapping("/getArticleTree")
    public Result getArticleTree(@RequestParam(value = "pid", required = false) Integer pid, @RequestParam(value = "keyword", required = false) String keyword) {
        return getResult(keyword, "CONTENTMANAGEMENT", 1, pid, null);
    }

    /**
     * web获取信箱的分类列表
     */
    @ApiOperation(value = "web获取信箱的分类列表")
    @GetMapping("/getWebMailboxTree")
    public Result getWebMailboxTree(@RequestParam(value = "pid", required = false) Integer pid, @RequestParam(value = "keyword", required = false) String keyword) {
        return getResult(keyword, "MAILBOX", null, pid, null);
    }

    /**
     * 管理后台获取文章的分类树形列表
     */
    @ApiOperation(value = "管理后台获取文章的分类树形列表")
    @GetMapping("/getAdminArticleTree")
    public Result getAdminArticleTree(@RequestParam(value = "pid", required = false) Integer pid, @RequestParam(value = "keyword", required = false) String keyword) {
        // 获取当前登录用户
        Integer loginId = StpUtil.getLoginIdAsInt();
        SysUser sysUser = iSysUserService.getById(loginId);
        if (sysUser == null) {
            return Result.error("用户未登录");
        }
        Integer roleId = null;
        if (sysUser.getRoleId() == 4) {
            roleId = 4;
        }
        return getResult(keyword, "CONTENTMANAGEMENT", 1, pid, roleId);
    }

    /**
     * web获取导航的分类树形列表
     */
    @ApiOperation(value = "web获取导航的分类树形列表")
    @GetMapping("/getNavigationTree")
    public Result getNavigationTree(@RequestParam(value = "pid", required = false) Integer pid, @RequestParam(value = "keyword", required = false) String keyword) {
        return getResult(keyword, "CONTENTMANAGEMENT", 2, pid, null);
    }

    /**
     * 获取友情链接的分类树形列表
     */
    @ApiOperation(value = "管理后台获取友情链接的分类树形列表")
    @GetMapping("/getFriendshipLinkTree")
    public Result getFriendshipLinkTree(@RequestParam(value = "pid", required = false) Integer pid, @RequestParam(value = "keyword", required = false) String keyword) {
        return getResult(keyword, "FRIENDSHIPLINK", null, pid, null);
    }

    /**
     * 获取信箱的分类树形列表
     */
    @ApiOperation(value = "管理后台获取信箱的分类树形列表")
    @GetMapping("/getMailboxTree")
    public Result getMailboxTree(@RequestParam(value = "pid", required = false) Integer pid, @RequestParam(value = "keyword", required = false) String keyword) {
        return getResult(keyword, "MAILBOX", null, pid, null);
    }

    private Result getResult(String keyword, String code, Integer type, Integer pid, Integer roleId) {
        QueryWrapper<SysCategory> queryWrapper = new QueryWrapper<>();
        if (type != null) {
            queryWrapper.eq("status", 1);
            if (type == 1) {
                queryWrapper.eq("is_article", 1);
            } else if (type == 2) {
                queryWrapper.eq("is_navigation", 1);
            }
        }
        if (roleId != null) {
            pid = 6;
        }
        queryWrapper.eq(pid != null, "pid", pid);
        queryWrapper.eq("code", code);
        queryWrapper.like(StrUtil.isNotBlank(keyword), "name", keyword);
        queryWrapper.orderByAsc("sort").orderByDesc("id");
        List<SysCategory> catModelList = categoryService.list(queryWrapper); // 全查或者根据name查
        List<SysCategory> categoryTree = categoryService.getCategoryTree(catModelList, pid);
        return Result.success(categoryTree);
    }

    /**
     * 新增内容管理的分类
     */
    @ApiOperation(value = "新增/编辑内容管理的分类")
    @PostMapping("/saveContentManagement")
    public Result saveContentManagement(@RequestBody SysCategory sysCategory) {
        Boolean result = getaBoolean(sysCategory, "CONTENTMANAGEMENT");
        return Result.success(result);
    }

    /**
     * 新增友情链接的分类
     */
    @ApiOperation(value = "新增/编辑友情链接的分类")
    @PostMapping("/saveFriendshipLink")
    public Result saveFriendshipLink(@RequestBody SysCategory sysCategory) {
        Boolean result = getaBoolean(sysCategory, "FRIENDSHIPLINK");
        return Result.success(result);
    }

    /**
     * 新增信箱分类
     */
    @ApiOperation(value = "新增/编辑信箱的分类")
    @PostMapping("/saveMailbox")
    public Result saveMailbox(@RequestBody SysCategory sysCategory) {
        Boolean result = getaBoolean(sysCategory, "MAILBOX");
        return Result.success(result);
    }

    private Boolean getaBoolean(SysCategory sysCategory, String code) {
        boolean result = false;
        if (sysCategory.getId() != null && sysCategory.getId() > 0) {
            sysCategory.setUpdateTime(new Date());
            result = this.categoryService.updateById(sysCategory);
        } else {
            sysCategory.setCode(code);
            sysCategory.setCreateTime(new Date());
            if (sysCategory.getPid() == null) {
                sysCategory.setPid(0);
            }
            result = this.categoryService.save(sysCategory);
        }
        if (result) {
            this.categoryService.initCategoryTree();
        }
        return result;
    }


    /**
     * 切换分类的状态（显示/隐藏）
     */
    @ApiOperation(value = "切换分类的状态（显示/隐藏）")
    @PostMapping("/toggleCategoryStatus")
    public Result toggleCategoryStatus(@RequestParam @NotNull Integer categoryId, @RequestParam boolean status) {
        try {
//            if (status && !isSpecialCategory(categoryId)) {
//                if (!checkFriendlyLink(categoryId)) {
//                    return Result.error("请先添加友情链接！");
//                }
//            }
            // 获取所有分类
            List<SysCategory> allCategories = categoryService.list(null);
            Map<Integer, SysCategory> categoryMap = new HashMap<>();
            Map<Integer, List<SysCategory>> parentChildMap = new HashMap<>();

            // 构建分类映射和父子关系映射
            for (SysCategory category : allCategories) {
                categoryMap.put(category.getId(), category);
                parentChildMap.computeIfAbsent(category.getPid(), k -> new ArrayList<>()).add(category);
            }

            // 切换状态
            toggleStatus(categoryId, status, categoryMap, parentChildMap);

            // 更新数据库
            for (SysCategory category : categoryMap.values()) {
                categoryService.updateById(category);
            }
            this.categoryService.initCategoryTree();
            return Result.success("状态切换成功");

        } catch (Exception e) {
            return Result.error("系统错误，请稍后再试");
        }
    }

    private boolean isSpecialCategory(Integer categoryId) {
        QueryWrapper<SysCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", categoryId);
        int count = categoryService.count(queryWrapper);
        return count > 0;
    }

    private boolean checkFriendlyLink(Integer categoryId) {
        try {
            QueryWrapper<FriendshipLink> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("c_id", categoryId);
            queryWrapper.eq("is_del", 0);
            // 使用 selectCount 只查询记录数，避免不必要的数据传输
            int count = this.linkService.count(queryWrapper);
            return count > 0;
        } catch (Exception e) {
            // 记录异常日志
            log.error(String.valueOf(e));
            // 根据业务需求决定是否抛出异常或返回默认值
            return false;
        }
    }

    private void toggleStatus(Integer categoryId, boolean status, Map<Integer, SysCategory> categoryMap, Map<Integer, List<SysCategory>> parentChildMap) {
        SysCategory category = categoryMap.get(categoryId);
        if (category == null) {
            return;
        }

        // 如果要隐藏父级，隐藏所有子级
        if (!status) {
            hideChildren(category, categoryMap, parentChildMap);
        }

        // 设置当前分类的状态
        category.setStatus(status);

        // 如果要显示子级，显示父级
        if (status) {
            showParents(category, categoryMap);
        }
    }

    private void hideChildren(SysCategory category, Map<Integer, SysCategory> categoryMap, Map<Integer, List<SysCategory>> parentChildMap) {
        List<SysCategory> children = parentChildMap.get(category.getId());
        if (children == null) {
            return;
        }
        for (SysCategory child : children) {
            child.setStatus(false);
            hideChildren(child, categoryMap, parentChildMap);
        }
    }

    private void showParents(SysCategory category, Map<Integer, SysCategory> categoryMap) {
        Integer parentId = category.getPid();
        while (parentId != null) {
            SysCategory parent = categoryMap.get(parentId);
            if (parent != null) {
                parent.setStatus(true);
                parentId = parent.getPid();
            } else {
                break;
            }
        }
    }

    @ApiOperation(value = "单位详情")
    @GetMapping("/getDetail")
    public Result<SysCategory> getDetail(Integer id) {
        SysCategory sysCategory = categoryService.getById(id);
        return Result.success(sysCategory);
    }


    /**
     * 列表
     *
     * @param code 代码
     * @return {@link Result}
     */
    @ApiOperation(value = "公用数据配置列表")
    @GetMapping("list")
    public Result list(@RequestParam(value = "code") String code) {
        QueryWrapper<SysCategory> sysCategoryQueryWrapper = new QueryWrapper<>();
        sysCategoryQueryWrapper.eq("code", code);
        sysCategoryQueryWrapper.ne("pid", 0);
        return Result.success(this.categoryService.list(sysCategoryQueryWrapper));
    }


    /**
     * 获取公共数据管理树
     *
     * @param pid pid
     * @return {@link Result}
     */
    @ApiOperation(value = "获取公共数据管理树")
    @GetMapping("/getPerTree")
    public Result getPerTree(@RequestParam(value = "pid", required = false) Integer pid,
                             @RequestParam(value = "keyword", required = false) String keyword) {
        QueryWrapper<SysCategory> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(pid != null, "pid", pid);
        queryWrapper.like(StrUtil.isNotBlank(keyword), "name", keyword);
        queryWrapper.eq("is_navigation", 0);
        queryWrapper.eq("is_article", 0);

        List<SysCategory> catModelList = categoryService.list(queryWrapper);//全查或者根据name查
        if (catModelList == null) {
            return Result.error("暂无数据");
        }
        List<SysCategory> categoryTree = categoryService.getCategoryTree(catModelList, pid);
        return Result.success(categoryTree);
    }

    /**
     * 添加和更新配置
     *
     * @param sysCategory 系统配置
     * @return {@link Result}
     */
    @ApiOperation(value = "添加和更新配置")
    @PostMapping("/addAndUpdateCategory")
    public Result addAndUpdateCategory(@RequestBody SysCategory sysCategory) {
        if (!StpUtil.hasPermission("admin:system:category:save")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        Boolean result = false;
        if (sysCategory.getId() != null && sysCategory.getId() > 0) {
            sysCategory.setUpdateTime(new Date());
            result = this.categoryService.updateById(sysCategory);
        } else {
            sysCategory.setCreateTime(new Date());
            if (sysCategory.getPid() == null) {
                sysCategory.setPid(0);
            }
            result = this.categoryService.save(sysCategory);
        }
        if (result) {
            this.categoryService.initCategoryTree();
        }
        return Result.success(result);
    }

    /**
     * 更新状态
     * 状态（开关操作显示、隐藏，注意父子之间联动，父级隐藏子级全部隐藏、父级显示不影响子级、子级隐藏不影响父级、子级显示父级也显示等）
     *
     * @param id     id
     * @param status 状态
     * @return {@link Result}<{@link Object}>
     */
    @ApiOperation(value = "根据公用数据配置表id更新启用禁用状态")
    @GetMapping("/updateCategoryStatus")//修改状态
    public Result updateStatus(@RequestParam("id") Integer id,
                               @RequestParam("status") boolean status) {
        if (!StpUtil.hasPermission("admin:system:category:status")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        SysCategory model = new SysCategory();
        model.setId(id);
        model.setStatus(status);
        categoryService.updateById(model);
        this.categoryService.initCategoryTree();
        return Result.success("修改成功");
    }

    /**
     * 删除配置
     *
     * @param id id
     * @return {@link Result}<{@link Object}>
     */
    @ApiOperation(value = "删除配置")
    @GetMapping("/deleteCategory")
    public Result deleteCategory(@RequestParam("id") Integer id) {
//        if (!StpUtil.hasPermission("admin:system:category:delete")) {
//            return Result.error(TotalConfig.NO_PERMISSION);
//        }
        SysCategory model = categoryService.getById(id);
        if (model == null) {
            return Result.error("该条数据不存在");
        } else {
            if (model.getPid() == 0) {
                categoryService.removeById(model.getId());
            }
            categoryService.removeById(id);
            this.categoryService.initCategoryTree();
            return Result.success(null);
        }
    }

    /**
     * 按id获取配置
     *
     * @param id      id
     * @param request 请求
     * @return {@link Result}<{@link Object}>
     */
    @ApiOperation(value = "按id获取配置")
    @GetMapping("/getCategoryById")
    public Result<Object> getCategoryById(Integer id, HttpServletRequest request) throws JsonProcessingException {
        if (!StpUtil.hasPermission("admin:system:category:detail")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        Object categoryObject = this.redisService.get(RedisConstant.CATEGORY_CACHE_PREFIX);
        if (categoryObject != null) {
            List<SysCategory> sysCategoryList = JSONUtil.toList(categoryObject.toString(), SysCategory.class);
            ObjectMapper mapper = new ObjectMapper();

            List<Map<String, Object>> data = mapper.readValue(JSONUtil.toJsonStr(sysCategoryList), new TypeReference<List<Map<String, Object>>>() {
            });

            // 构建 id 到节点的映射缓存
            Map<Integer, Map<String, Object>> idCache = buildIdCache(data);

            // 查询 pid = 29 的子数组
            List<Map<String, Object>> result = getChildrenByPid(id, idCache);
            return Result.success(result);
        }
        return Result.success(null);
    }


    // 构建 id 到节点的映射缓存
    public static Map<Integer, Map<String, Object>> buildIdCache(List<Map<String, Object>> data) {
        Map<Integer, Map<String, Object>> idCache = new HashMap<>();
        Queue<Map<String, Object>> queue = new LinkedList<>(data);
        while (!queue.isEmpty()) {
            Map<String, Object> node = queue.poll();
            idCache.put((Integer) node.get("id"), node);
            List<Map<String, Object>> children = (List<Map<String, Object>>) node.getOrDefault("child", Collections.emptyList());
            queue.addAll(children);
        }
        return idCache;
    }

    // 根据 pid 直接查询子数组
    public static List<Map<String, Object>> getChildrenByPid(int pid, Map<Integer, Map<String, Object>> idCache) {
        if (idCache.containsKey(pid)) {
            return (List<Map<String, Object>>) idCache.get(pid).getOrDefault("child", Collections.emptyList());
        }
        return Collections.emptyList();
    }


    /**
     * 按id获取配置
     *
     * @param id id
     * @return {@link Result}<{@link Object}>
     */
    @ApiOperation(value = "详情")
    @GetMapping("/details")
    public Result<Object> details(@RequestParam(value = "id") Integer id) {
        if (!StpUtil.hasPermission("admin:system:category:detail")) {
//            return Result.error(TotalConfig.NO_PERMISSION);
        }

        return Result.success(this.categoryService.getById(id));
    }

    /**
     * 获取指定分类的父级和子集数据
     */
    @ApiOperation(value = "获取指定分类的父级和子集数据")
    @GetMapping("/getCategoryTreeById")
    public Result getCategoryTreeById(@RequestParam @NotNull Integer id) {
        List<SysCategory> categoryList = categoryService.getCategoryTreeById(id);
        if (categoryList != null) {
            return Result.success(categoryList);
        } else {
            return Result.error("分类不存在");
        }
    }
}

