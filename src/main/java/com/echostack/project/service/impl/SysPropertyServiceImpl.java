package com.echostack.project.service.impl;

import com.echostack.project.dao.SysPropertyMapper;
import com.echostack.project.infra.exception.ServiceException;
import com.echostack.project.model.entity.SysProperty;
import com.echostack.project.service.SysPropertyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: Echo
 * @Date: 2019/4/4 19:51
 * @Description:
 */
@Service
@Transactional
@Slf4j
public class SysPropertyServiceImpl implements SysPropertyService {

    @Autowired
    private SysPropertyMapper sysPropertyMapper;


    @Override
    public boolean add(SysProperty sysProperty) {
        boolean bool = false;
        if(sysPropertyMapper.findByPropId(sysProperty.getPropertyId()) != null){
            throw new ServiceException("属性id："+sysProperty.getPropertyId()+"，已存在");
        }else{
            if(sysPropertyMapper.add(sysProperty) > 0){
                bool = true;
            }
        }
        return bool;
    }

    @Override
    public boolean deleteById(Long id) {
        boolean bool = false;
        if(id == null){
            throw new ServiceException("删除属性失败，id不能为空");
        }else if(sysPropertyMapper.deleteById(id) > 0){
            bool = true;
        }
        return bool;
    }

    @Override
    public boolean update(SysProperty sysProperty) {
        boolean bool = false;
        if(sysPropertyMapper.update(sysProperty) > 0){
            bool = true;
        }
        return bool;
    }

    @Override
    public List<SysProperty> findAll() {
        return sysPropertyMapper.findAll();
    }

    public PageInfo<List<SysProperty>> findByPaging(Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<SysProperty> sysProperties = sysPropertyMapper.findAll();
        return new PageInfo(sysProperties);
    }

    @Override
    public SysProperty findByPropId(String propertyId) {
        return sysPropertyMapper.findByPropId(propertyId);
    }

    @Override
    public SysProperty get(Long id) {
        return sysPropertyMapper.get(id);
    }
}
