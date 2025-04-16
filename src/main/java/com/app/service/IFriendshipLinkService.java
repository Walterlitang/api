package com.app.service;

import com.app.ResponseVo.ArtResponse;
import com.app.model.FriendshipLink;
import com.app.model.SysCategory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 友情链接表 服务类
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
public interface IFriendshipLinkService extends IService<FriendshipLink> {

    IPage<FriendshipLink> adminPage(Page<FriendshipLink> mailboxPage, String linkAddress,Integer cid);

    List<SysCategory> webList(Integer type);

    boolean isExistLinkAddress(Integer cId);

    ArtResponse webArtList();
}
