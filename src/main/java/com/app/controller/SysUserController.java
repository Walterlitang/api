package com.app.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.app.aspect.AutoLog;
import com.app.common.RedisConstant;
import com.app.common.TotalConfig;
import com.app.model.SysCategory;
import com.app.model.SysRole;
import com.app.model.SysUser;
import com.app.service.ISysCategoryService;
import com.app.service.ISysRoleService;
import com.app.service.ISysUserService;
import com.app.service.RedisService;
import com.app.util.CacheUtil;
import com.app.util.IpUtil;
import com.app.util.Result;
import com.app.vo.LoginRequestVO;
import com.app.vo.RegisterRequestVO;
import com.app.vo.SysUserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.captcha.SpecCaptcha;
import com.pig4cloud.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static cn.hutool.crypto.digest.DigestUtil.md5Hex;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yoominic
 * @since 2023-12-05
 */
@Api(tags = "admin用户接口")
@RestController
@RequestMapping("/adminUser")
public class SysUserController {
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ISysCategoryService iSysCategoryService;

    /**
     * 验证码
     *
     * @return {@link Result}
     * @throws Exception 例外
     */
    @ApiOperation(value = "获得验证码")
    @GetMapping("/captcha")
    public Result<Object> captcha() {
        // 生成图片验证码
        SpecCaptcha specCaptcha = new SpecCaptcha(90, 36, 4);
        // 设置字体（如果有默认字体，可以不用设置）
        specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 30));
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        String text = specCaptcha.text().toLowerCase();
        // 替换文本中的数字1
        text = replaceDigitOne(text);
        // 设置背景颜色
        UUID uuid = UUID.randomUUID();
        // 保存验证码到本地缓存
        CacheUtil.put(uuid.toString(), text);
        JSONObject data = new JSONObject();
        String base64 = specCaptcha.toBase64();
        data.put("key", uuid);
        data.put("code", base64);
        return Result.success(data);
    }

    /**
     * 替换文本中的数字1为其他数字
     *
     * @param text 验证码文本
     * @return 替换后的验证码文本
     */
    private String replaceDigitOne(String text) {
        char[] chars = {'0', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c == '1') {
                int index = RandomUtil.randomInt(chars.length);
                sb.append(chars[index]);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // todo 测试方便，上线务必删掉
/*    @ApiOperation(value = "测试拿token")
    @GetMapping("/token")
    public Result<Object> token() {
        String password = "JTRQ2024!@";
        String username = "admin";
        String salt = "n2yk3do5";
        String md5Password = md5Hex(password + salt);
        SysUser user = iSysUserService.login(username, md5Password);
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        return Result.success(token);
    }*/


    /**
     * 登录
     *
     * @param loginRequest 登录请求
     * @param request      请求
     * @return {@link Result }
     */
    @ApiOperation(value = "登录")
    @PostMapping("/login")
    @AutoLog("用户登录")
    public Result login(@RequestBody @Valid LoginRequestVO loginRequest, HttpServletRequest request) {

        Object code1 = CacheUtil.get(loginRequest.getKey());
        if (code1 == null) {
            return Result.error("验证码已失效或已过期");
        }
        if (!code1.equals(loginRequest.getCode())) {
            return Result.error("验证码错误");
        }
        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        sysUserQueryWrapper.eq("username", loginRequest.getUsername());
        SysUser u = this.iSysUserService.getOne(sysUserQueryWrapper);
        if (u == null) {
            return Result.error("用户名或密码错误");
        }
        String salt = u.getSalt();
        String md5Password = md5Hex(loginRequest.getPassword() + salt);

        SysUser user = iSysUserService.login(loginRequest.getUsername(), md5Password);
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        int status = user.getStatus();
        if (status == 0) {
            return Result.error("该账户已被禁用");
        }

        // 根据 rememberMe 设置 Token 有效期
        if (loginRequest.isRememberMe()) {
            StpUtil.login(user.getId(), 7 * 24 * 60 * 60); // 7天有效期
        } else {
            StpUtil.login(user.getId()); // 默认有效期
        }
        String token = StpUtil.getTokenValue();

        String ip = IpUtil.getIpAddress(request);
        user.setToken(token);
        user.setLastLoginIp(ip);
        user.setLastLoginTime(LocalDateTime.now());
        iSysUserService.updateById(user);
        user.setPassword(null);
        this.redisService.set(RedisConstant.USER_TOKEN_CACHE_PREFIX + token, user.getUsername());
        return Result.success(user);
    }


    /**
     * 注册
     *
     * @param registerRequestVO 注册请求
     * @param request           请求
     * @return {@link Result }
     */
    @ApiOperation(value = "注册")
    @PostMapping("/register")
    @AutoLog("用户注册")
    public Result register(@RequestBody @Valid RegisterRequestVO registerRequestVO, HttpServletRequest request) {
        Object code1 = CacheUtil.get(registerRequestVO.getKey());
        if (code1 == null) {
            return Result.error("验证码已失效或已过期");
        }
        if (!code1.equals(registerRequestVO.getCode())) {
            return Result.error("验证码错误");
        }
        // 检查用户名是否已存在
        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        sysUserQueryWrapper.eq("username", registerRequestVO.getUsername());
        SysUser existingUser = this.iSysUserService.getOne(sysUserQueryWrapper);
        if (existingUser != null) {
            return Result.error("用户名已存在");
        }
        // 创建新用户
        SysUser newUser = new SysUser();
        newUser.setUsername(registerRequestVO.getUsername());
        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        String md5Password = md5Hex(registerRequestVO.getPassword() + salt);
        newUser.setPassword(md5Password);
        newUser.setSalt(salt);
        newUser.setRoleId(registerRequestVO.getRoleId());
        newUser.setStatus(1); // 设置账户状态为启用
        // 保存新用户到数据库
        boolean saveResult = iSysUserService.save(newUser);
        if (!saveResult) {
            return Result.error("注册失败");
        }
        // 登录新用户
        StpUtil.login(newUser.getId());
        String token = StpUtil.getTokenValue();
        String ip = IpUtil.getIpAddress(request);
        newUser.setToken(token);
        newUser.setLastLoginIp(ip);
        newUser.setLastLoginTime(LocalDateTime.now());
        iSysUserService.updateById(newUser);
        newUser.setPassword(null);
        this.redisService.set(RedisConstant.USER_TOKEN_CACHE_PREFIX + token, newUser.getUsername());
        return Result.success(newUser);
    }


    public static void main(String[] args) {
        String salt = "874139b7aca449e89354c3cfa80b4b92";
        String md5Password = md5Hex("InfoMap2025!@" + salt);
        System.out.println(md5Password);
    }

    /**
     * 注销
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "注销")
    @GetMapping("/logOut")
    @AutoLog("用户注销")
    public Result logOut() {
        String tokenValue = StpUtil.getTokenValue();
        StpUtil.kickout(tokenValue);
        return Result.success(null);
    }

    /**
     * 退出登录
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "退出登录")
    @GetMapping("/loginOn")
    public Result loginOn() {
        StpUtil.logout();
        return Result.success("退出成功");
    }

    /**
     * 通过令牌获取管理信息
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "通过令牌获取管理信息")
    @GetMapping("/getAdminInfoByToken")
    public Result getAdminInfoByToken() {
        SysUser sysUser = iSysUserService.getById(StpUtil.getLoginIdAsLong());
        if (sysUser == null) {
            return Result.error("用户不存在");
        }
        SysRole sysRole = iSysRoleService.getById(sysUser.getRoleId());
        String[] roleArr = sysRole.getRules().split(",");//获取字符串
        List<String> roleModelList = new ArrayList<>(Arrays.asList(roleArr));
        sysUser.setPermissionList(roleModelList);
        return Result.success(sysUser);
    }

    /**
     * 获取管理员列表
     *
     * @param page    第页
     * @param limit   限制
     * @param keyword 关键字
     * @return {@link Result}
     */
    @ApiOperation(value = "获取管理员列表")
    @GetMapping("/getAdminList")
    public Result getAdminList(Integer page, Integer limit, String keyword) {
        SysUser sysUser = iSysUserService.getById(StpUtil.getLoginIdAsLong());
        if (sysUser == null) {
            return Result.error("用户不存在");
        }
        SysUserVo adminUserList = iSysUserService.getAdminList(page, limit, keyword);
        return Result.success(adminUserList);
    }

    /**
     * 按id获取管理员
     *
     * @param id id
     * @return {@link Result}
     */
    @ApiOperation(value = "按id获取管理员")
    @GetMapping("/getAdminById")
    public Result getAdminById(Integer id) {
        if (!StpUtil.hasPermission("admin:system:admin:detail")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        SysUser sysUser = iSysUserService.getById(StpUtil.getLoginIdAsLong());
        if (sysUser == null) {
            return Result.error("用户不存在");
        }
        SysUser SysUser = iSysUserService.getById(id);
        SysUser.setPassword(null);
        return Result.success(SysUser);
    }

    /**
     * 后台更新密码
     *
     * @param user 密码
     * @return {@link Result}
     */
    @ApiOperation(value = "后台更新密码")
    @PostMapping("/updatePassWord")
    @AutoLog("后台更新密码")
    public Result updatePassWord(@RequestBody SysUser user) {
        SysUser sysUser = iSysUserService.getById(StpUtil.getLoginIdAsLong());
        if (sysUser == null) {
            return Result.error("用户不存在");
        }
        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        String newWord = md5Hex(user.getPassword() + salt);
        sysUser.setPassword(newWord);
        sysUser.setSalt(salt);
        iSysUserService.updateById(sysUser);
        return Result.success("修改成功");
    }


    /**
     * 添加和更新管理员
     *
     * @param sysUser sys用户
     * @return {@link Result}
     */
    @ApiOperation(value = "添加和更新管理员")
    @PostMapping("/addAndUpdateAdmin")
    @AutoLog("添加和更新管理员")
    public Result addAndUpdateAdmin(@RequestBody SysUser sysUser) {
        if (!StpUtil.hasPermission("admin:system:admin:save")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        SysUser sysUserById = iSysUserService.getById(StpUtil.getLoginIdAsLong());
        if (sysUserById == null) {
            return Result.error("用户不存在");
        }
        if (sysUser.getDepartmentId() != null) {
            sysUser.setDepartmentName(iSysCategoryService.getById(sysUser.getDepartmentId()).getName());
        }
        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        if (sysUser.getId() == null) {
            String password = sysUser.getPassword();
            String newPassWord = md5Hex(password + salt);
            sysUser.setPassword(newPassWord);
            sysUser.setCreateTime(LocalDateTime.now());
            sysUser.setSalt(salt);
            iSysUserService.save(sysUser);
        } else {
            SysUser user = iSysUserService.getById(sysUser.getId());
            if (user != null) {
                sysUser.setId(user.getId());
            } else {
                return Result.success("用户不存在");
            }
            if (sysUser.getPassword() != null) {
                String password = sysUser.getPassword();
                String newPassWord = md5Hex(password + salt);
                sysUser.setPassword(newPassWord);
                sysUser.setSalt(salt);
            }
            sysUser.setUpdateTime(LocalDateTime.now());
            iSysUserService.updateById(sysUser);
        }
        return Result.success("操作成功");
    }

    /**
     * 更新管理员状态
     *
     * @param id     id
     * @param status 状态
     * @return {@link Result}
     */
    @ApiOperation(value = "更新管理员状态")
    @GetMapping("/updateAdminStatus")
    @AutoLog("更新管理员状态")
    public Result updateAdminStatus(@RequestParam("id") Integer id, @RequestParam("status") Integer status) {
        if (!StpUtil.hasPermission("admin:system:admin:status")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        SysUser sysUser = iSysUserService.getById(StpUtil.getLoginIdAsLong());
        if (sysUser == null) {
            return Result.error("用户不存在");
        }
        SysUser SysUser = new SysUser();
        SysUser.setId(id);
        SysUser.setStatus(status);
        iSysUserService.updateById(SysUser);
        return Result.success("操作成功");
    }


    /**
     * 删除admin
     * del admin
     *
     * @param id id
     * @return {@link Result}
     */
    @ApiOperation(value = "删除admin")
    @GetMapping("/delAdmin")
    @AutoLog("删除用户")
    public Result deleteAdmin(Integer id) {
        if (!StpUtil.hasPermission("admin:system:admin:delete")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        SysUser sysUser = iSysUserService.getById(StpUtil.getLoginIdAsLong());
        if (sysUser == null) {
            return Result.error("用户不存在");
        }
        iSysUserService.removeById(id);
        return Result.success("删除成功");
    }

    /**
     * 获取首长列表
     *
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "获取首长列表")
    @GetMapping("/getChiefList")
    public Result getChiefList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<SysUser> sysUserPage = new Page<>(page, size);
        IPage<SysUser> chiefList = iSysUserService.getChiefList(sysUserPage);
        return Result.success(chiefList);
    }

    /**
     * 获取单位列表
     *
     * @return
     */
    @ApiOperation(value = "获取单位列表")
    @GetMapping("/getUnitList")
    public Result getUnitList() {
        List<SysCategory> sysUnitList = iSysUserService.getUnitList();
        return Result.success(sysUnitList);
    }
}

