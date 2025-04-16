package com.app.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.app.pojo.User;
import com.app.service.IUserService;
import com.app.util.IpUtil;
import com.app.util.Result;
import com.app.util.WechatUtil;
import com.app.vo.UserLoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author yoominic
 * @since 2024-07-29
 */
@Api(tags = "小程序人员相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IpUtil ipUtil;

    /**
     * 获取openid
     *
     * @param code
     * @return {@link Result}<{@link UserLoginVo}>
     * @throws LoginException
     */
    @ApiOperation(value = "获取openid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "小程序code值", dataTypeClass = String.class)
    })
    @GetMapping("/getOpenid")
    public Result getOpenid(String code) throws LoginException {
        //微信登录
        return Result.success(iUserService.getOpenid(code));
    }

    /**
     * 获取手机号
     *
     * @param code
     * @return {@link Result}<{@link Object}>
     */
    @ApiOperation(value = "获取手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "小程序code值", dataTypeClass = String.class),
            @ApiImplicitParam(name = "mobileCode", value = "手机验证码", dataTypeClass = String.class)
    })
    @GetMapping("/getPhone")
    @Transactional(rollbackFor = Exception.class)
    public Result getPhone(String code, String mobileCode) throws LoginException {
        UserLoginVo user = iUserService.getPhone(code, mobileCode);
        return Result.success(user);
    }

    /**
     * 获取用户电话
     *
     * @param code 代码
     * @return {@link Result}
     */
    @ApiOperation(value = "获取用户电话")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "小程序code值", dataTypeClass = String.class)
    })
    @GetMapping("/getUserPhone")
    public Result getUserPhone(String code) {
        return Result.success(WechatUtil.getMobile(code));
    }


    /**
     * 个人信息查询
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "个人信息查询")
    @GetMapping("/selectUserById")
    public Result selectUserById() {
        User userById = iUserService.selectUserById(StpUtil.getLoginIdAsLong());
        if (userById == null) {
            return Result.error("用户不存在");
        }
        userById.setFaceUrl("/file/" + userById.getFaceUrl());
        return Result.success(userById);
    }

    /**
     * 更新用户头像
     *
     * @param faceUrl 人脸url
     * @return {@link Result}
     */
    @ApiOperation(value = "更新用户头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "更新用户头像", value = "图片地址", dataTypeClass = String.class)
    })
    @GetMapping("/updateUserFace")
    @Transactional(rollbackFor = Exception.class)
    public Result updateUserFace(String faceUrl) {
        if (!StpUtil.isLogin()) {
            return Result.error("未登录");
        }
        User userById = iUserService.getById(String.valueOf(StpUtil.getLoginId()));
        if (userById == null) {
            return Result.error("用户不存在");
        }
        String[] split = faceUrl.split("/");
        if (split.length > 0) {
            userById.setFaceUrl(split[split.length - 1]);
        }
        iUserService.updateById(userById);
        return Result.success("操作成功");
    }

    /**
     * 验证登录
     *
     * @param phone
     * @return {@link Result}
     */
    @ApiOperation(value = "验证登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataTypeClass = String.class)
    })
    @GetMapping("/verifyLogin")
    public Result verifyLogin(String phone, HttpServletRequest request) {
        String ip = IpUtil.getIpAddress(request);
        return Result.success(iUserService.verifyLogin(phone, ip));
    }


}

