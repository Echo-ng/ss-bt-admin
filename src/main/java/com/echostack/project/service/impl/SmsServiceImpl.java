package com.echostack.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.exceptions.ClientException;
import com.echostack.project.dao.SysPropertyMapper;
import com.echostack.project.infra.constant.Application;
import com.echostack.project.infra.constant.Security;
import com.echostack.project.infra.exception.ServiceException;
import com.echostack.project.infra.util.WebUtil;
import com.echostack.project.manager.SmsManager;
import com.echostack.project.model.dto.SmsResponseDto;
import com.echostack.project.service.SmsService;
import com.echostack.project.service.SysPropertyService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Echo
 * @Date: 2019/4/10 23:41
 * @Description:
 */
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsManager smsManager;

    @Autowired
    private SysPropertyService sysPropertyService;


    public void sendSms(String mobile,String code) throws ClientException {
        String templateCode = sysPropertyService.findByPropId(Application.SMS_CODE_TEMPLATE_CODE).getPropertyValue();
        String signName = sysPropertyService.findByPropId(Application.SMS_CODE_SIGN_NAME).getPropertyValue();
        CommonResponse commonResponse = smsManager.SendSms(mobile,code,templateCode,signName);
        if(!Strings.isNullOrEmpty(commonResponse.getData())){
            SmsResponseDto smsResponseDto = JSON.parseObject(commonResponse.getData(),SmsResponseDto.class);
            if(!"OK".equals(smsResponseDto.getCode())){
                throw new ServiceException(smsResponseDto.getMessage());
            }
        }else{
            throw new ServiceException("短信厂商未成功返回消息，请联系管理员！");
        }
    }
}
