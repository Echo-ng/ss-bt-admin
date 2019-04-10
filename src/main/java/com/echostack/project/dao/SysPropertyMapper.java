package com.echostack.project.dao;

import com.echostack.project.model.entity.SysProperty;

import java.util.List;

/**
 * @Author: Echo
 * @Date: 2019/4/4 19:33
 * @Description:
 */
public interface SysPropertyMapper {

    int add(SysProperty sysProperty);

    int deleteById(Long id);

    int update(SysProperty sysProperty);

    List<SysProperty> findAll();

    SysProperty findByPropId(String propertyId);

    SysProperty get(Long id);
}
