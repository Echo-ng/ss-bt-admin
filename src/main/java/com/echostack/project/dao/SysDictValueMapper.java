package com.echostack.project.dao;

import com.echostack.project.model.entity.SysDictValue;

import java.util.List;

/**
 * @Author: Echo
 * @Date: 2019/4/3 15:05
 * @Description:
 */
public interface SysDictValueMapper {

    SysDictValue add(SysDictValue sysDictValue);

    int addByBatch(List<SysDictValue> sysDictValues);

    int deleteById(Long id);

    int deleteByDictId(Long dictId);

    int update(SysDictValue sysDictValue);

    List<SysDictValue> findAll();

    SysDictValue get(Long id);

    SysDictValue findByCode(String dictCode,String valueCode);

    List<SysDictValue> findByDictId(Long dictId);
}
