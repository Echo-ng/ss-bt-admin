package com.echostack.project.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author: Echo
 * @Date: 2019/3/24 20:10
 * @Description:
 */
@Data
public abstract class BaseEntity {

    private Long id;

    private Date gmtCreate = new Date();

    private Date gmtModified = new Date();
}
