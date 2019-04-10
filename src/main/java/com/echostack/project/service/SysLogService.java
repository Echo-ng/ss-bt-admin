package com.echostack.project.service;

import com.echostack.project.model.entity.SysLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * @Author: Echo
 * @Date: 2019/3/24 20:40
 * @Description:
 */
public interface SysLogService {

    @Async
    void add(ProceedingJoinPoint joinPoint, SysLog log);

    List<SysLog> findAll();

    int deleteById(Long id);
}
