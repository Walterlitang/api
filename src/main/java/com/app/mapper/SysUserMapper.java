package com.app.mapper;

import com.app.model.SysCategory;
import com.app.model.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 管理后台用户 Mapper 接口
 * </p>
 *
 * @author yoominic
 * @since 2024-07-29
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    IPage<SysUser> getChiefList(Page<SysUser> sysUserPage);

    List<SysCategory> getUnitList();

}
