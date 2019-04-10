package com.echostack.project.model.dto;

import lombok.Data;

/**
 * @Author: Echo
 * @Date: 2019/4/9 10:54
 * @Description:
 */
@Data
public class SmsTemplateParamDto {
    private String code;
//    private Integer time;

    public SmsTemplateParamDto(String code) {
        this.code = code;
//        this.time = time;
    }

    public SmsTemplateParamDto(){

    }
}
