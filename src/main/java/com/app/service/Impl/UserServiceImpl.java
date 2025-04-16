package com.app.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.app.exception.BaseException;
import com.app.mapper.UserMapper;
import com.app.pojo.User;
import com.app.service.IUserService;
import com.app.util.IpUtil;
import com.app.util.WechatUtil;
import com.app.vo.UserLoginVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author yoominic
 * @since 2024-07-29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取openid
     *
     * @param code 代码
     * @return {@link User}
     * @throws LoginException 登录异常
     */
    @Override
    public Object getOpenid(String code) throws LoginException {
        //获取openid
        JSONObject object = WechatUtil.getOpenid(code);
        String openid = object.getString("openid");
        if (openid == null) {
            throw new LoginException("openid为空");
        }
        //根据openid查询用户信息
        List<User> userList = getUserByOpenid(openid);
        if (userList.isEmpty()) {
            User user = new User();
            user.setOpenId(openid);
            user.setName("燃气用户" + openid.substring(0, 4));
            user.setFaceUrl("/file/face.jpg");
            user.setCreateTime(LocalDateTime.now());
            user.setStatus(1);
            user.setUnionId(object.getString("unionid"));
            userMapper.insert(user);
            StpUtil.login(user.getId());
            String token = StpUtil.getTokenValue();
            UserLoginVo userLoginVO = new UserLoginVo();
            ArrayList<User> list = new ArrayList<>();
            list.add(user);
            userLoginVO.setUserList(list);
            userLoginVO.setToken(token);
            return userLoginVO;
        } else {
            for (User user : userList) {
                user.setLoginKey(RandomUtil.randomString(8));
                userMapper.updateById(user);
                user.setFaceUrl("/file/" + user.getFaceUrl());
            }
            if (userList.size() > 1) {
                StpUtil.login(userList.get(0).getId());
                String token = StpUtil.getTokenValue();
                UserLoginVo userLoginVO = new UserLoginVo();
                userLoginVO.setUserList(Collections.singletonList(userList.get(0)));
                userLoginVO.setToken(token);
                return userLoginVO;
            } else {
                StpUtil.login(userList.get(0).getId());
                String token = StpUtil.getTokenValue();
                UserLoginVo userLoginVO = new UserLoginVo();
                userLoginVO.setUserList(userList);
                userLoginVO.setToken(token);
                return userLoginVO;
            }
        }
    }

    /**
     * 通过openid获取用户
     *
     * @param openid openid
     * @return {@link List}<{@link User}>
     */
    public List<User> getUserByOpenid(String openid) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("open_id", openid);
        try {
            return this.userMapper.selectList(userQueryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new BaseException(500, "操作异常，请联系管理员");
    }


    public User getUserByMobile(String mobile) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("mobile", mobile);
        try {
            return this.userMapper.selectOne(userQueryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new BaseException(500, "操作异常，请联系管理员");
    }


    @Override
    public UserLoginVo getPhone(String code, String mobileCode) throws LoginException {
        UserLoginVo userLoginVO = new UserLoginVo();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) Objects.requireNonNull(requestAttributes).resolveReference(RequestAttributes.REFERENCE_REQUEST);
        JSONObject object = WechatUtil.getOpenid(code);
        String openid = object.getString("openid");
        String unionid = object.getString("unionid");
        if (openid == null) {
            throw new LoginException("openid为空");
        }
        String mobile = WechatUtil.getMobile(mobileCode);
        User user = getUserByMobile(mobile);
        if (user == null) {
            //当前是新用户，自动注册
            user = new User();
            user.setMobile(mobile);
            user.setName("燃气用户" + mobile.substring(5, 11));
            user.setFaceUrl("face.jpg");
            user.setStatus(1);
            user.setOpenId(openid);
            user.setUnionId(unionid);
            user.setGroupId(1L);
            user.setSource(0);
            user.setCreateTime(LocalDateTime.now());
            userMapper.insert(user);
        } else {
            user.setOpenId(openid);
            user.setUnionId(unionid);
            assert request != null;
            user.setLoginIp(IpUtil.getIpAddress(request));
            user.setLastLoginTime(LocalDateTime.now());
            userMapper.updateById(user);
        }
        user.setFaceUrl("/file/" + user.getFaceUrl());
        userLoginVO.setUser(user);
        userLoginVO.getUser().setOpenId(null);
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        userLoginVO.setToken(token);
        return userLoginVO;
    }

    @Override
    public User selectUserById(Long userId) {
        return userMapper.selectById(userId);
    }


    @Override
    public UserLoginVo verifyLogin(String phone, String ip) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("mobile", phone);
        User user = userMapper.selectOne(userQueryWrapper);
        user.setLoginIp(ip);
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);
        if (user != null) {
            StpUtil.login(user.getId());
            String token = StpUtil.getTokenValue();
            UserLoginVo userLoginVO = new UserLoginVo();
            userLoginVO.setUser(user);
            userLoginVO.setToken(token);
            return userLoginVO;
        } else {
            throw new BaseException(400, "该用户不存在");
        }
    }


    public void updateUser(User user) {
        userMapper.updateById(user);
    }

}
