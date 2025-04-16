package com.app.conf;

import cn.dev33.satoken.stp.StpInterface;
import com.app.mapper.SysRoleMapper;
import com.app.model.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {


    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        SysRole sysRole = sysRoleMapper.selectRoleById(Integer.parseInt((String) loginId));
        String rules = sysRole.getRules();
        String[] split = rules.split(",");
        return Arrays.asList(split);
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {

        SysRole sysRole = sysRoleMapper.selectRoleById((Integer) loginId);
        String rules = sysRole.getRules();
        String[] split = rules.split(";");
        return Arrays.asList(split);
    }


}
