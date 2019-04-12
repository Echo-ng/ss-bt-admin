package com.echostack.project.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Echo
 * @Date: 2019/4/3 22:42
 * @Description:
 */
@Data
public class UserPasswordLoginDto implements Serializable {
    private String username;
    private String password;
    private Integer type;//0 用户名/手机号/邮箱+密码登录 1 手机号+验证码登录
}
