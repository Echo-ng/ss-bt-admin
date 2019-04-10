package com.echostack.project.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.echostack.project.dao.SysPropertyMapper;
import com.echostack.project.model.entity.SysProperty;
import com.echostack.project.service.SysPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Echo
 * @Date: 2019/4/8 18:56
 * @Description:
 */
@Configuration
public class AliSDKConfig {

    private final String ALIYUN_SDK_ACCESSKEY_ID = "aliyun.sdk.AccessKeyId";
    private final String ALIYUN_SDK_ACCESSKEY_SECRET = "aliyun.sdk.AccessKeySecret";

    @Autowired
    private SysPropertyService sysPropertyService;

    @Bean
    public IAcsClient iAcsClient(){
        SysProperty accessKeyId = sysPropertyService.findByPropId(ALIYUN_SDK_ACCESSKEY_ID);
        SysProperty accessKeySecret = sysPropertyService.findByPropId(ALIYUN_SDK_ACCESSKEY_SECRET);
        DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId.getPropertyValue(), accessKeySecret.getPropertyValue());
        return new DefaultAcsClient(profile);
    }
}
