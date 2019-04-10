package com.echostack.project.service;

import com.aliyuncs.exceptions.ClientException;

/**
 * @Author: Echo
 * @Date: 2019/4/11 0:23
 * @Description:
 */
public interface SmsService {
    void sendSms(String mobile,String code) throws ClientException;
}
