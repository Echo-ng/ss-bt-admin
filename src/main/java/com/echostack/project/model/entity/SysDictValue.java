package com.echostack.project.model.entity;

import lombok.Data;

/**
 * @Author: Echo
 * @Date: 2019/4/3 10:53
 * @Description:
 */
@Data
public class SysDictValue extends BaseEntity {
    private String name; //名称
    private String code; //编码
    private String description; //描述
    private Boolean isEnabled; //是否启用
    private Long dictId; //字典id
}
