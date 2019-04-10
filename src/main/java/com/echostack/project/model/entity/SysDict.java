package com.echostack.project.model.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author: Echo
 * @Date: 2019/4/3 10:49
 * @Description:
 */
@Data
public class SysDict extends BaseEntity {
    private String name; //名称
    private String code; //编码
    private String description; //描述
    private Boolean isEnabled; //是否启用
    private List<SysDictValue> dictValues; //字典信息
}
