package com.app.service.Impl;

import com.app.mapper.FileClassMapper;
import com.app.model.FileClassModel;
import com.app.service.FileClassService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class FileClassServiceImpl implements FileClassService {
    @Autowired
    FileClassMapper fileClassMapper;

    @Override
    public List<FileClassModel> getModelList() {
        QueryWrapper<FileClassModel> queryWrapper = new QueryWrapper<>();
        return fileClassMapper.selectList(queryWrapper);
    }

    @Override
    public List<FileClassModel> getModelListByPid(int pid) {
        QueryWrapper<FileClassModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", pid);
        return fileClassMapper.selectList(queryWrapper);
    }
}
