package com.echostack.project.web;

import com.echostack.project.infra.annotation.Logger;
import com.echostack.project.infra.constant.Application;
import com.echostack.project.infra.constant.Security;
import com.echostack.project.infra.dto.Result;
import com.echostack.project.infra.dto.ResultGenerator;
import com.echostack.project.infra.util.IPUtil;
import com.echostack.project.infra.util.WebUtil;
import com.echostack.project.model.dto.SmsCodeDto;
import com.echostack.project.service.SmsService;
import com.echostack.project.service.SysPropertyService;
import com.echostack.project.service.impl.SmsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Echo
 * @Date: 2019/4/11 0:22
 * @Description:
 */
@RestController
@RequestMapping("/sms")
@Api(tags = {"短信功能"})
public class SmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private SysPropertyService sysPropertyService;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

//    @Autowired
//    private RedisTemplate redisTemplate;

    @GetMapping("/code")
    @Logger("发送短信验证码")
    @ApiOperation(value = "短信验证码", notes = "发送短信验证码给用户")
    public Result smsCode(HttpServletRequest request, HttpServletResponse response, @ApiParam(required = true, value = "手机号") String mobile){
        Result result = ResultGenerator.genSuccessResult();
        //判断手机号格式是否规范
        Pattern p = Pattern.compile(Security.REGEX_MOBILE);

        Matcher m = p.matcher(mobile);
        if(m.matches()){
            //获取当前设置的验证码配置
            String length = sysPropertyService.findByPropId(Application.SMS_CODE_LENGTH).getPropertyValue();
            String source = sysPropertyService.findByPropId(Application.SMS_CODE_SOURCE).getPropertyValue();
            String expire = sysPropertyService.findByPropId(Application.SMS_CODE_EXPIRE).getPropertyValue();
            //生成验证码
            String code = WebUtil.createCode(Integer.parseInt(length),source);
            //存到会话中
            SmsCodeDto codeDto = new SmsCodeDto(code,Integer.parseInt(expire));
            sessionStrategy.setAttribute(new ServletWebRequest(request),Application.SESSION_KEY_SMS_CODE+mobile,codeDto);
            try {
                smsService.sendSms(mobile,code);
            }catch (Exception e){
                e.printStackTrace();
                result = ResultGenerator.genFailResult("发送验证码失败,"+e.getMessage());
            }
        }else{
            result = ResultGenerator.genFailResult("手机号格式错误");
        }
        return result;
    }
}
