package com.app.service.Impl;

import com.app.mapper.AttachmentMapper;
import com.app.model.AttachmentModel;
import com.app.model.FileClassModel;
import com.app.service.AttachmentService;
import com.app.service.FileClassService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class AttachmentServiceImpl implements AttachmentService {
    @Autowired
    AttachmentMapper attachmentMapper;
    @Autowired
    FileClassService fileClassService;

    @Override
    public AttachmentModel getAttById(int id) {
        QueryWrapper<AttachmentModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.select().eq("id", id);
        AttachmentModel role = attachmentMapper.selectOne(queryWrapper);
        if (role == null) {
            return null;
        } else {
            return role;
        }
    }

    @Override
    public Page<AttachmentModel> getPageAttList(int page, int limit, Integer pid, Integer type) {
        QueryWrapper<AttachmentModel> queryWrapper = new QueryWrapper<>();

        if (pid != null && pid > 0) {
            List<Integer> idList = new ArrayList<>();
            idList.add(pid);
            List<FileClassModel> fileClassModels = fileClassService.getModelListByPid(pid);
            if (fileClassModels != null && fileClassModels.size() > 0) {
                for (int i = 0; i < fileClassModels.size(); i++) {
                    idList.add(fileClassModels.get(i).getId());
                    List<FileClassModel> fileClassModels2 = fileClassService.getModelListByPid(fileClassModels.get(i).getId());
                    if (fileClassModels2 != null && fileClassModels2.size() > 0) {
                        for (int j = 0; j < fileClassModels2.size(); j++) {
                            idList.add(fileClassModels2.get(j).getId());
                        }
                    }

                }
            }
            queryWrapper.in("pid", idList);
        }

        queryWrapper.eq("file_type", type).orderByDesc("id");
        Page<AttachmentModel> pages = new Page<>();
        pages.setCurrent(page);
        pages.setSize(limit);
        Page<AttachmentModel> roleList = attachmentMapper.selectPage(pages, queryWrapper);
        return roleList;
    }

    @Override
    public void update(AttachmentModel model) {
        attachmentMapper.updateById(model);
    }

    @Override
    public void insert(AttachmentModel model) {
        attachmentMapper.insert(model);
    }

    @Override
    public void delete(int id) {
        attachmentMapper.deleteById(id);
    }

    @Override
    public Integer count() {
        QueryWrapper<AttachmentModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id");
        Integer testCount = attachmentMapper.selectCount(queryWrapper);
        return testCount;
    }
}
