package com.echostack.project.manager;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.echostack.project.model.dto.SmsCodeDto;
import com.echostack.project.model.dto.SmsTemplateParamDto;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @Author: Echo
 * @Date: 2019/4/9 9:37
 * @Description:
 */
@Component
public class SmsManager {

    @Value("${app.login.code.source}")
    private String smsCodeSource;

    @Value("${app.login.code.expire}")
    private Integer smsExpireSeconds;

    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    @Autowired
    private IAcsClient iAcsClient;

    public CommonResponse SendSms(String phoneNumbers,String code,String templateCode,String signName) throws ClientException {
        CommonRequest request = new CommonRequest();
        request.setConnectTimeout(10000);
        request.setReadTimeout(10000);
        request.setMethod(MethodType.POST);
        request.setDomain(DOMAIN);
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers",phoneNumbers);
        request.putQueryParameter("TemplateParam", JSON.toJSONString(new SmsTemplateParamDto(code)));
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        return iAcsClient.getCommonResponse(request);
    }
}
