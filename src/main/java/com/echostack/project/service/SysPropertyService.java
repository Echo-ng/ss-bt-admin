package com.echostack.project.service;

import com.echostack.project.model.entity.SysProperty;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author: Echo
 * @Date: 2019/4/4 19:50
 * @Description:
 */
public interface SysPropertyService {

    boolean add(SysProperty sysProperty);

    boolean deleteById(Long id);

    boolean update(SysProperty sysProperty);

    List<SysProperty> findAll();

    PageInfo<List<SysProperty>> findByPaging(Integer pageNum,Integer pageSize);

    SysProperty findByPropId(String propertyId);

    SysProperty get(Long id);
}
