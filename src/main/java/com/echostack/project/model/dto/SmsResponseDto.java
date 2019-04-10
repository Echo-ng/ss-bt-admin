package com.echostack.project.model.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @Author: Echo
 * @Date: 2019/4/10 0:24
 * @Description:
 */
@Data
public class SmsResponseDto {

    @JSONField(name = "Message")
    private String message;

    @JSONField(name = "RequestId")
    private String requestId;

    @JSONField(name = "BizId")
    private String bizId;

    @JSONField(name = "Code")
    private String code;
}
