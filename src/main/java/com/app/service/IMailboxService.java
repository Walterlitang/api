package com.app.service;

import com.app.model.Mailbox;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 信箱管理表 服务类
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
public interface IMailboxService extends IService<Mailbox> {

    IPage<Mailbox> adminPage(Page<Mailbox> mailboxPage, String title, Integer userId,Integer type,Integer isReply,Integer categoryId);
}
