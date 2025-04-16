package com.app.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.app.aspect.AutoLog;
import com.app.common.SystemConstant;
import com.app.common.TotalConfig;
import com.app.model.Person;
import com.app.model.SysUser;
import com.app.service.IPersonService;
import com.app.service.ISysUserService;
import com.app.util.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;

import static cn.hutool.crypto.digest.DigestUtil.md5Hex;

/**
 * <p>
 * 心理人员信息表 前端控制器
 * </p>
 *
 * @author yoominic
 * @since 2025-02-21
 */
@Slf4j
@Api(tags = "心理人员信息表")
@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private IPersonService personService;
    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 新增或编辑人员信息
     *
     * @param person 人员信息对象
     * @return 操作结果
     */
    @ApiOperation(value = "新增或编辑人员信息")
    @AutoLog("新增或编辑人员信息")
    @PostMapping("/saveOrUpdate")
    @Transactional
    public Result saveOrUpdate(@RequestBody @Valid Person person) {
        if (!StpUtil.hasPermission("admin:person:save")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }

        try {
            // 输入验证
            if (person == null) {
                return Result.error("人员信息对象无效");
            }

            boolean savedOrUpdated;
            if (person.getId() != null) {
                savedOrUpdated = this.personService.updateById(person);
            } else {
                person.setCreateTime(LocalDateTime.now());
                savedOrUpdated = this.personService.save(person);
            }

            if (savedOrUpdated) {
                // 生成管理员账号，绑定关系
                SysUser newUser = new SysUser();
                newUser.setUsername(person.getName());
                String salt = UUID.randomUUID().toString().replaceAll("-", "");
                String md5Password = md5Hex(SystemConstant.DEFAULT_PASSWORD + salt);
                newUser.setPassword(md5Password);
                newUser.setSalt(salt);
                newUser.setRoleId(5);
                newUser.setStatus(1); // 设置账户状态为启用
                newUser.setPersonId(person.getId()); // 账号绑定咨询师

                // 保存新用户到数据库
                boolean saveResult = iSysUserService.save(newUser);
                if (!saveResult) {
                    return Result.error("人员信息保存或更新失败:新增账号失败");
                }
                return Result.success("人员信息保存或更新成功");
            } else {
                return Result.error("人员信息保存或更新失败");
            }
        } catch (IllegalArgumentException e) {
            return Result.error("非法参数: " + e.getMessage());
        } catch (DataAccessException e) {
            return Result.error("数据库操作失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("系统错误", e);
            return Result.error("系统错误，请稍后再试");
        }
    }

    /**
     * 删除人员信息
     *
     * @param id 人员信息ID
     * @return 操作结果
     */
    @ApiOperation(value = "删除人员信息")
    @AutoLog("删除人员信息")
    @GetMapping("/delete")
    public Result delete(Integer id) {
        if (!StpUtil.hasPermission("admin:person:delete")) {
            return Result.error(TotalConfig.NO_PERMISSION);
        }
        try {
            // 获取人员信息
            Person person = this.personService.getById(id);
            if (person == null) {
                return Result.error("该人员信息不存在");
            }

            // 设置删除标志并更新
            person.setIsDel(1);
            boolean updateSuccess = this.personService.updateById(person);

            if (updateSuccess) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败，请稍后再试");
            }
        } catch (Exception e) {
            return Result.error("系统错误，请稍后再试");
        }
    }

    /**
     * 根据ID查询人员信息
     *
     * @param id 人员信息ID
     * @return 人员信息对象
     */
    @ApiOperation(value = "查询人员信息详情")
    @GetMapping("/info")
    public Result info(Integer id) {
        Person person = personService.getById(id);
        if (person == null) {
            return Result.error("人员不存在");
        }
        //根据人员id查询用户动态、回复

        // 更新人员浏览量
        person.setViewCount(person.getViewCount() != null ? person.getViewCount() + 1 : 1);
        boolean updateSuccess = personService.updateById(person);
        if (!updateSuccess) {
            return Result.error("更新浏览量失败");
        }
        return Result.success(person);
    }

    /**
     * 查询所有人员信息
     *
     * @return 人员信息列表
     */
    @ApiOperation(value = "分页查询人员信息列表")
    @GetMapping("/list")
    public Result page(@RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                       @RequestParam(value = "keyword", required = false) String keyword) {
        Page<Person> personPage = new Page<>(page, size);
        return Result.success(this.personService.adminPage(personPage, keyword));
    }

    /**
     * 修改人员信息状态
     *
     * @param id     人员信息ID
     * @param status 人员信息状态
     */
    @ApiOperation(value = "修改人员信息状态")
    @AutoLog("修改人员信息状态")
    @GetMapping("/status")
    public Result updateArticleStatus(Integer id, Integer status) {
        Person person = this.personService.getById(id);
        if (person == null) {
            return Result.error("人员信息不存在");
        }
        person.setStatus(status);
        boolean updated = this.personService.updateById(person);
        if (updated) {
            return Result.success("人员信息状态修改成功");
        } else {
            return Result.error("人员信息状态修改失败");
        }
    }

    /**
     * 查询web页人员信息  查询排序前3张
     *
     * @return 查询web页人员信息
     */
    @ApiOperation(value = "查询web页人员信息")
    @GetMapping("/webPage")
    public Result webPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                          @RequestParam(value = "size", defaultValue = "3") Integer size,
                          @RequestParam(value = "type") Integer type) {
        Page<Person> personPage = new Page<>(page, size);
        return Result.success(this.personService.webPage(personPage, type));
    }

    /**
     * 查询人员类型列表
     *
     * @return
     */
    @ApiOperation(value = "查询人员类型列表")
    @GetMapping("/personTypeList")
    public Result personTypeList() {
        return Result.success(this.personService.personTypeList());
    }

}

