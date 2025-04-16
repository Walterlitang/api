package com.app.service;

import com.app.model.Person;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 人员信息表 服务类
 * </p>
 *
 * @author yoominic
 * @since 2025-02-21
 */
public interface IPersonService extends IService<Person> {

    IPage<Person> adminPage(Page<Person> personPage, String title);

    IPage<Person> webPage(Page<Person> personPage,Integer type);

    List<Person> personTypeList();
}
