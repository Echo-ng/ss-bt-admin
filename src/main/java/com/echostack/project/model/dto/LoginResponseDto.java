package com.echostack.project.model.dto;

import com.echostack.project.model.entity.SysUser;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Echo
 * @Date: 2019/4/3 10:02
 * @Description:
 */
@Data
public class LoginResponseDto implements Serializable {
    private SysUser sysUser; //返回用戶信息
    private String token;//token信息
}
