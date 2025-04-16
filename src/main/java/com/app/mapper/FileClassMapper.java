package com.app.mapper;


import com.app.model.FileClassModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Component
@Mapper
public interface FileClassMapper extends BaseMapper<FileClassModel> {
}
