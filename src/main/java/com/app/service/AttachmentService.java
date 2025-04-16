package com.app.service;

import com.app.model.AttachmentModel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface AttachmentService {

    AttachmentModel getAttById(int id);

    Page<AttachmentModel> getPageAttList(int page, int limit, Integer pid, Integer type);//文件管理分页查询

    void update(AttachmentModel model);

    void insert(AttachmentModel model);

    void delete(int id);

    Integer count();

}
