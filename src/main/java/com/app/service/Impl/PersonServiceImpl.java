package com.app.service.Impl;

import com.app.mapper.PersonMapper;
import com.app.model.Person;
import com.app.service.IPersonService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 人员信息表 服务实现类
 * </p>
 *
 * @author yoominic
 * @since 2025-02-21
 */
@Service
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements IPersonService {

    @Override
    public IPage<Person> adminPage(Page<Person> personPage, String name) {
        return this.baseMapper.adminPage(personPage, name);
    }

    @Override
    public IPage<Person> webPage(Page<Person> personPage, Integer type) {
        return this.baseMapper.webPage(personPage,type);
    }

    @Override
    public List<Person> personTypeList() {
        return this.baseMapper.personTypeList();
    }
}
