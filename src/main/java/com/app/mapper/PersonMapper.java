package com.app.mapper;

import com.app.model.Person;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 人员信息表 Mapper 接口
 * </p>
 *
 * @author yoominic
 * @since 2025-02-21
 */
public interface PersonMapper extends BaseMapper<Person> {

    IPage<Person> adminPage(Page<Person> personPage, String name);

    IPage<Person> webPage(Page<Person> personPage,Integer type);

    List<Person> personTypeList();

}
