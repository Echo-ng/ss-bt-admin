package com.echostack.project.service.impl;

import com.echostack.project.dao.SysDictValueMapper;
import com.echostack.project.model.entity.SysDictValue;
import com.echostack.project.service.SysDictValueService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: Echo
 * @Date: 2019/4/3 15:34
 * @Description:
 */
@Service
@Transactional
@Slf4j
public class SysDictValueServiceImpl implements SysDictValueService {

    @Autowired
    private SysDictValueMapper sysDictValueMapper;

    @Override
    public SysDictValue add(SysDictValue sysDictValue) {
        return sysDictValueMapper.add(sysDictValue);
    }

    @Override
    public boolean deleteById(Long id) {
        boolean bool = false;
        if(sysDictValueMapper.deleteById(id) > 0){
            bool = true;
        }
        return bool;
    }

    @Override
    public boolean deleteByDictId(Long dictId) {
        boolean bool = false;
        if(sysDictValueMapper.deleteByDictId(dictId) > 0){
            bool = true;
        }
        return bool;
    }

    @Override
    public boolean update(SysDictValue sysDictValue) {
        boolean bool = false;
        if(sysDictValueMapper.update(sysDictValue) > 0){
            bool = true;
        }
        return bool;
    }

    @Override
    public List<SysDictValue> findAll() {
        return sysDictValueMapper.findAll();
    }

    @Override
    public SysDictValue get(Long id) {
        return sysDictValueMapper.get(id);
    }

    @Override
    public SysDictValue findByCode(String dictCode, String valueCode) {
        return sysDictValueMapper.findByCode(dictCode,valueCode);
    }

    @Override
    public PageInfo<List<SysDictValue>> findByPaging(Long dictId, Integer pageNum, Integer pageSize) {
        //分页
        PageHelper.startPage(pageNum, pageSize);
        List<SysDictValue> dictValues = sysDictValueMapper.findByDictId(dictId);
        return new PageInfo(dictValues);
    }

    @Override
    public PageInfo<List<SysDictValue>> findByPaging(Integer pageNum, Integer pageSize) {
        //分页
        PageHelper.startPage(pageNum, pageSize);
        List<SysDictValue> dictValues = sysDictValueMapper.findAll();
        return new PageInfo(dictValues);
    }

}
