package com.echostack.project.service;

import com.echostack.project.model.entity.SysDictValue;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author: Echo
 * @Date: 2019/4/3 15:33
 * @Description:
 */
public interface SysDictValueService {

    SysDictValue add(SysDictValue sysDictValue);

    boolean deleteById(Long id);

    boolean deleteByDictId(Long dictId);

    boolean update(SysDictValue sysDictValue);

    List<SysDictValue> findAll();

    SysDictValue get(Long id);

    SysDictValue findByCode(String dictCode,String valueCode);

    PageInfo<List<SysDictValue>> findByPaging(Long dictId,Integer pageNum,Integer pageSize);

    PageInfo<List<SysDictValue>> findByPaging(Integer pageNum,Integer pageSize);
}
