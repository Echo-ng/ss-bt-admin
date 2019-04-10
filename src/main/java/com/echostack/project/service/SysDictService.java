package com.echostack.project.service;

import com.echostack.project.model.entity.SysDict;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author: Echo
 * @Date: 2019/4/3 15:29
 * @Description:
 */
public interface SysDictService {

    boolean add(SysDict sysDict);

    boolean deleteById(Long id);

    boolean update(SysDict sysDict);

    List<SysDict> findAll();

    SysDict findByCode(String code);

    SysDict get(Long id);

    PageInfo<List<SysDict>> findByPaging(Integer pageNum, Integer pageSize);
}
