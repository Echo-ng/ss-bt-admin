package com.echostack.project.model.dto;

import lombok.Data;

/**
 * @Author: Echo
 * @Date: 2019/4/3 17:20
 * @Description:
 */
@Data
public class PageDto<T> {
    private Integer pageNum;
    private Integer pageSize;
    private T param;
}
