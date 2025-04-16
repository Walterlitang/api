package com.app.mapper;

import com.app.model.Mailbox;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 信箱管理表 Mapper 接口
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
public interface MailboxMapper extends BaseMapper<Mailbox> {

    IPage<Mailbox> adminPage(Page<Mailbox> mailboxPage,
                             @Param("title") String title,
                             @Param("userId") Integer userId,
                             @Param("type") Integer type,
                             @Param("isReply") Integer isReply,
                             @Param("categoryId") Integer categoryId);
}
