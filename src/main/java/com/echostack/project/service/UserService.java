package com.echostack.project.service;

import com.echostack.project.model.entity.SysUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    SysUser findByUsername(String username);

    SysUser loadUserByMobile(String mobile);

    SysUser add(SysUser sysUser);

    int deleteById(Long id);

    int update(SysUser sysUser);

    List<SysUser> findAll();

//    String saveUserLoginInfo(SysUser sysUser);

}
