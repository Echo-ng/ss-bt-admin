package com.echostack.project.service.impl;

import com.echostack.project.dao.SysDictMapper;
import com.echostack.project.dao.SysDictValueMapper;
import com.echostack.project.infra.exception.ServiceException;
import com.echostack.project.model.entity.SysDict;
import com.echostack.project.model.entity.SysDictValue;
import com.echostack.project.service.SysDictService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Echo
 * @Date: 2019/4/3 15:31
 * @Description:
 */
@Service
@Transactional
@Slf4j
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;

    @Autowired
    private SysDictValueMapper sysDictValueMapper;

    @Override
    public boolean add(SysDict sysDict) {
        boolean bool = false;
        if(sysDictMapper.add(sysDict) > 0){
            if(sysDict.getDictValues() != null && sysDict.getDictValues().size() > 0){
                sysDict.getDictValues().parallelStream().forEach(item -> {
                    item.setDictId(sysDict.getId());
                });
                if(sysDictValueMapper.addByBatch(sysDict.getDictValues()) > 0){
                    bool = true;
                }
            }else{
                bool = true;
            }
        }
        return bool;
    }

    @Override
    public boolean deleteById(Long id) {
        boolean bool = false;
        //先删除值表中对应的记录
        if(sysDictMapper.deleteById(id) > 0){
            if(sysDictValueMapper.deleteByDictId(id) > 0){
                bool = true;
            }
        }
        return bool;
    }

    @Override
    public boolean update(SysDict sysDict) {
        boolean bool = false;
        if(sysDictMapper.update(sysDict) > 0){
            if(sysDict.getDictValues() != null && sysDict.getDictValues().size() > 0){
                //先删除所有的再新增
                if(sysDictValueMapper.deleteByDictId(sysDict.getId()) > 0){
                    if(sysDictValueMapper.addByBatch(sysDict.getDictValues()) > 0){
                        bool = true;
                    }
                }
            }
        }
        return bool;
    }

    @Override
    public List<SysDict> findAll() {
        return sysDictMapper.findAll();
    }

    @Override
    public SysDict findByCode(String code) {
        return sysDictMapper.findByCode(code);
    }

    @Override
    public SysDict get(Long id) {
        return sysDictMapper.get(id);
    }

    @Override
    public PageInfo<List<SysDict>> findByPaging(Integer pageNum, Integer pageSize) {
        //分页
        PageHelper.startPage(pageNum,pageSize);
        List<SysDict> list = sysDictMapper.findAll();
        return new PageInfo(list);
    }

}
