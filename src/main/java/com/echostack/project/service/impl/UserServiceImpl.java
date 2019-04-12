package com.echostack.project.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.echostack.project.dao.SysUserMapper;
import com.echostack.project.infra.constant.Security;
import com.echostack.project.infra.exception.ServiceException;
import com.echostack.project.model.entity.SysUser;
import com.echostack.project.service.UserService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by CodeGenerator on 2019/02/21.
 */

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    public PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        SysUser sysUser = sysUserMapper.findByUsername(s);
//        if (sysUser == null) {
//            throw new UsernameNotFoundException("用户名不存在");
//        }
//        else{
//            sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
//        }
        SysUser sysUser;
        if(Strings.isNullOrEmpty(s)){
            throw new ServiceException("username不能为空");
        }else{
            Pattern p = Pattern.compile(Security.REGEX_EMAIl);
            Matcher m = p.matcher(s);
            if(m.matches()){
                sysUser = sysUserMapper.findByEmail(s);
            }else{
                p = Pattern.compile(Security.REGEX_MOBILE);
                m = p.matcher(s);
                if(m.matches()){
                    sysUser = sysUserMapper.findByMobile(s);
                }else{
                    sysUser = sysUserMapper.findByUsername(s);
                }
            }
        }
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        return sysUser;
    }



    public SysUser findByUsername(String username){
//        SysUser sysUser;
//        if(Strings.isNullOrEmpty(username)){
//            throw new ServiceException("username不能为空");
//        }else{
//            Pattern p = Pattern.compile(Security.REGEX_EMAIl);
//            Matcher m = p.matcher(username);
//            if(m.matches()){
//                sysUser = sysUserMapper.findByEmail(username);
//            }else{
//                p = Pattern.compile(Security.REGEX_MOBILE);
//                m = p.matcher(username);
//                if(m.matches()){
//                    sysUser = sysUserMapper.findByMobile(username);
//                }else{
//                    sysUser = sysUserMapper.findByUsername(username);
//                }
//            }
//        }
        return sysUserMapper.findByUsername(username);
    }

    @Override
    public SysUser loadUserByMobile(String mobile) {
        SysUser sysUser = new SysUser();
        Pattern p = Pattern.compile(Security.REGEX_MOBILE);
        Matcher m = p.matcher(mobile);
        if(m.matches()){
            sysUser = sysUserMapper.findByMobile(mobile);
            if (sysUser == null) {
                throw new UsernameNotFoundException("手机号不存在");
            }
        }else{
            throw new ServiceException("手机格式不正确");
        }
        return sysUser;
    }

    @Override
    public SysUser add(SysUser sysUser) {
        SysUser result = null;
        //密码加密
        sysUser.setUsername(passwordEncoder.encode(sysUser.getUsername()));
        try {
            result = sysUserMapper.add(sysUser);
        } catch (Exception e) {
            String errorMsg = "保存用户信息出错，"+e.getMessage();
            log.error(errorMsg);
        }
        return result;
    }

    @Override
    public int deleteById(Long id) {
        return this.sysUserMapper.deleteById(id);
    }

    @Override
    public int update(SysUser sysUser) {
        return this.sysUserMapper.update(sysUser);
    }

    @Override
    public List<SysUser> findAll() {
        return this.sysUserMapper.findAll();
    }

//    @Override
//    public String saveUserLoginInfo(SysUser sysUser) {
//        String token = "";
//        try {
//            Algorithm algorithm = Algorithm.HMAC256(Security.TOKEN_SECRET);
//            token = JWT.create()
//                    .withIssuer(Security.CLAIM_KEY)
//                    .sign(algorithm);
//        } catch (JWTCreationException exception){
//            String errorMsg = "创建token出错"+exception.getMessage();
//            log.error(errorMsg);
//        }
//        return token;
//    }
}
