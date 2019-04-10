package com.echostack.project.web;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.exceptions.ClientException;
import com.echostack.project.infra.annotation.Logger;
import com.echostack.project.infra.constant.Application;
import com.echostack.project.infra.constant.Security;
import com.echostack.project.infra.dto.Result;
import com.echostack.project.infra.dto.ResultCode;
import com.echostack.project.infra.dto.ResultGenerator;
import com.echostack.project.infra.util.WebUtil;
import com.echostack.project.manager.SmsManager;
import com.echostack.project.model.dto.SmsCodeDto;
import com.echostack.project.model.dto.SmsResponseDto;
import com.echostack.project.model.dto.UserPasswordLoginDto;
import com.echostack.project.model.entity.SysUser;
import com.echostack.project.service.SysPropertyService;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Echo
 * @Date: 2019/2/27 11:29
 * @Description:
 */
@RestController
@Api(tags = {"登录"})
public class LoginController {

    @ApiOperation(value = "用户密码登录", notes = "用户名密码登录,username可以是 用户名/手机号/邮箱")
    @PostMapping("/signIn")
    public Result<SysUser> login(@ApiParam(required = true, value = "登录信息")
                                @RequestBody UserPasswordLoginDto userPasswordLoginDto) {
        Result<SysUser> result = new Result<>();
        result.setCode(ResultCode.SUCCESS).setMessage("会员:"+ userPasswordLoginDto.getUsername()+"，登录成功！");
        result.setData(new SysUser(userPasswordLoginDto.getUsername()));
        return result;
    }
}
