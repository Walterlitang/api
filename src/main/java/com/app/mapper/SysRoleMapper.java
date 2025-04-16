package com.app.mapper;

import com.app.model.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author yoominic
 * @since 2024-07-29
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select("select r.*\n" +
            "from lm_sys_role r\n" +
            "left join  lm_sys_user u on r.id = u.role_id\n" +
            "where u.id = #{userid}")
    SysRole selectRoleById(Integer userid);

}
