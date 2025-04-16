package com.app.service;

import com.app.pojo.User;
import com.app.vo.UserLoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.security.auth.login.LoginException;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author yoominic
 * @since 2024-07-29
 */
public interface IUserService extends IService<User> {

    Object getOpenid(String code) throws LoginException;

    UserLoginVo getPhone(String code, String mobileCode) throws LoginException;

    UserLoginVo verifyLogin(String phone, String ip);

    User selectUserById(Long userId);


}
