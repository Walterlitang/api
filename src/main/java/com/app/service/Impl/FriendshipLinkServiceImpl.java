package com.app.service.Impl;

import com.app.ResponseVo.ArtResponse;
import com.app.mapper.FriendshipLinkMapper;
import com.app.model.FriendshipLink;
import com.app.model.SysCategory;
import com.app.service.IFriendshipLinkService;
import com.app.service.ISysCategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 友情链接表 服务实现类
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
@Service
public class FriendshipLinkServiceImpl extends ServiceImpl<FriendshipLinkMapper, FriendshipLink> implements IFriendshipLinkService {

    @Autowired
    private ISysCategoryService categoryService;

    @Override
    public IPage<FriendshipLink> adminPage(Page<FriendshipLink> mailboxPage, String linkAddress, Integer cid) {
        return this.baseMapper.adminPage(mailboxPage, linkAddress, cid);
    }

    /**
     * type
     * 1 热点专题
     * 2 办事大厅
     * 3 站点导航
     * 4 时政新闻轮播
     * 5 导航下方
     * 6 天天读报
     */
    private static final int TYPE_1_ROOT_ID = 75;
    private static final int TYPE_2_ROOT_ID = 69;
    private static final int TYPE_3_ROOT_ID = 49;
    private static final int TYPE_4_ROOT_ID = 125;
    private static final int TYPE_5_ROOT_ID = 129;
    private static final int TYPE_6_ROOT_ID = 131;

    private static final Map<Integer, Integer> TYPE_TO_ROOT_ID_MAP = new HashMap<Integer, Integer>() {{
        put(1, TYPE_1_ROOT_ID);
        put(2, TYPE_2_ROOT_ID);
        put(3, TYPE_3_ROOT_ID);
        put(4, TYPE_4_ROOT_ID);
        put(5, TYPE_5_ROOT_ID);
        put(6, TYPE_6_ROOT_ID);
    }};

    @Override
    public List<SysCategory> webList(Integer type) {
        if (type == null || !TYPE_TO_ROOT_ID_MAP.containsKey(type)) {
            throw new IllegalArgumentException("Invalid type: " + type);
        }

        // 根据 type 查询对应的数据
        Integer rootId = TYPE_TO_ROOT_ID_MAP.get(type);
        if (type == 4 || type == 5 || type == 6 || type == 1) {
            return categoryService.getCategoryTree(this.baseMapper.webListByRootId(rootId), 0);
        }
        return categoryService.getCategoryTree(this.baseMapper.webList(), rootId);
    }

    @Override
    public boolean isExistLinkAddress(Integer cId) {
        try {
            QueryWrapper<FriendshipLink> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("c_id", cId);
            queryWrapper.eq("is_del", 0);
            // 使用 selectCount 只查询记录数，避免不必要的数据传输
            Integer count = this.baseMapper.selectCount(queryWrapper);
            return count > 0;
        } catch (Exception e) {
            // 记录异常日志
            log.error("Error checking existence of link address for cId: {}");
            // 根据业务需求决定是否抛出异常或返回默认值
            return false;
        }
    }

    @Override
    public ArtResponse webArtList() {
        ArtResponse artResponse = new ArtResponse();
        try {
            // 并行执行数据库查询以提高性能
            List<SysCategory> theaterList = this.baseMapper.theaterList();
            List<SysCategory> movieList = this.baseMapper.movieList();
            List<SysCategory> otherList = this.baseMapper.otherList();

            // 设置查询结果到响应对象
            artResponse.setTheaterList(theaterList);
            artResponse.setMovieList(movieList);
            artResponse.setOtherList(otherList);
        } catch (Exception e) {
            // 记录异常日志
            log.error("查询祁连艺苑列表失败", e);
            // 抛出异常或返回默认值，具体取决于业务需求
            throw new RuntimeException("查询祁连艺苑列表失败", e);
        }
        return artResponse;
    }


}
