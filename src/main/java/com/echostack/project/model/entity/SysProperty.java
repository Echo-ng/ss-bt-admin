package com.echostack.project.model.entity;

import lombok.Data;

/**
 * @Author: Echo
 * @Date: 2019/4/4 19:35
 * @Description:
 */
@Data
public class SysProperty extends BaseEntity {

    private String propertyId;

    private String propertyValue;

    private String name;

    private Boolean isEnabled;
}
