package com.echostack.project.model.entity;

import lombok.Data;

/**
 * @Author: Echo
 * @Date: 2019/3/24 20:10
 * @Description:
 */

@Data
public class SysLog extends BaseEntity {
    private String opDescription;//操作说明
    private Long time;//操作耗时
    private String method;//操作对应的方法
    private String param;//调用方法的参数
    private String ip;//ip地址
    private String username;//用户名称
    private String location;//位置信息
}
