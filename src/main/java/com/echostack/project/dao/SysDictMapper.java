package com.echostack.project.dao;

import com.echostack.project.model.entity.SysDict;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Author: Echo
 * @Date: 2019/4/3 10:58
 * @Description:
 */
public interface SysDictMapper {

    int add(SysDict sysDict);

    int deleteById(Long id);

    int update(SysDict sysDict);

    List<SysDict> findAll();

    SysDict findByCode(String code);

    SysDict get(Long id);
}
