package com.app.mapper;

import com.app.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author yoominic
 * @since 2024-07-29
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
