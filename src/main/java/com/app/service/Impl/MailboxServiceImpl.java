package com.app.service.Impl;

import com.app.mapper.MailboxMapper;
import com.app.model.Mailbox;
import com.app.service.IMailboxService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 信箱管理表 服务实现类
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
@Service
public class MailboxServiceImpl extends ServiceImpl<MailboxMapper, Mailbox> implements IMailboxService {

    @Override
    public IPage<Mailbox> adminPage(Page<Mailbox> mailboxPage, String title, Integer userId, Integer type,Integer isReply,Integer categoryId) {
        return this.baseMapper.adminPage(mailboxPage, title, userId,type,isReply,categoryId);
    }
}
