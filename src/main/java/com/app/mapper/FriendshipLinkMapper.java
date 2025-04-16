package com.app.mapper;

import com.app.model.FriendshipLink;
import com.app.model.SysCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 友情链接表 Mapper 接口
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
public interface FriendshipLinkMapper extends BaseMapper<FriendshipLink> {

    IPage<FriendshipLink> adminPage(Page<FriendshipLink> mailboxPage, @Param("linkAddress") String linkAddress,@Param("cid") Integer cid);

    List<SysCategory> webList();

    List<SysCategory> webListByRootId(@Param("rootId") Integer rootId);

    List<SysCategory> theaterList();

    List<SysCategory> movieList();

    List<SysCategory> otherList();
}
