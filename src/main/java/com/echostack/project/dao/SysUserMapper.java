package com.echostack.project.dao;

import com.echostack.project.model.entity.SysUser;

import java.util.List;

public interface SysUserMapper {

    SysUser findByUsername(String username);

    SysUser add(SysUser sysUser);

    int deleteById(Long id);

    int update(SysUser sysUser);

    List<SysUser> findAll();

    SysUser findByMobile(String mobile);

    SysUser findByEmail(String email);
}