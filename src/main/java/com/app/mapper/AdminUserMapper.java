package com.app.mapper;


import com.app.model.AdminUserModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Component
@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUserModel> {
}
