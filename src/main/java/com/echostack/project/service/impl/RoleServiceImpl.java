package com.echostack.project.service.impl;

import com.echostack.project.dao.SysRoleMapper;
import com.echostack.project.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: Echo
 * @Date: 2019/2/26 15:27
 * @Description:
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

}
