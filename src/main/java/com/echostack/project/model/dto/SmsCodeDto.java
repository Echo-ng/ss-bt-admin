package com.echostack.project.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: Echo
 * @Date: 2019/4/9 15:00
 * @Description:
 */
@Data
public class SmsCodeDto {
    private String code;
    private LocalDateTime expireTime;

    public SmsCodeDto(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    public SmsCodeDto(String code,Integer expireIn){
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public SmsCodeDto() {
    }
}
