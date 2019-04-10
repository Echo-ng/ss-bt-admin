package com.echostack.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.echostack.project.dao.SysLogMapper;
import com.echostack.project.infra.annotation.Logger;
import com.echostack.project.infra.util.IPUtil;
import com.echostack.project.model.entity.SysLog;
import com.echostack.project.service.SysLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author: Echo
 * @Date: 2019/3/24 20:41
 * @Description:
 */
@Service
@Transactional
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public void add(ProceedingJoinPoint joinPoint, SysLog log) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Logger logAnnotation = method.getAnnotation(Logger.class);
        if (logAnnotation != null) {
            // 注解上的描述
            log.setOpDescription(logAnnotation.value());
        }
        // 请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 请求的方法名
        String methodName = signature.getName();
        log.setMethod(className + "." + methodName + "()");
        // 请求的方法参数值
        Object[] args = joinPoint.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            try {
                log.setParam(handleParams(args, Arrays.asList(paramNames)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Date curretDate = new Date();
        log.setGmtCreate(curretDate);
        log.setGmtModified(curretDate);
        log.setLocation(IPUtil.getCityInfo(log.getIp()));
        // 保存系统日志
        sysLogMapper.add(log);
    }

    private String handleParams(Object[] args, List paramNames) {
        JSONObject jsonObject = new JSONObject();
        if(args.length == 1){
            return JSON.toJSONString(args[0]);
        }
        for (int i = 0; i < args.length; i++) {
            if(!(args[i] instanceof ServletRequest) && !(args[i] instanceof ServletResponse)){
                jsonObject.put((String) paramNames.get(i),JSON.parse(JSON.toJSONString(args[i])));
            }

//            if (args[i] instanceof Map) {
//                Set set = ((Map) args[i]).keySet();
//                List list = new ArrayList();
//                List paramList = new ArrayList<>();
//                for (Object key : set) {
//                    list.add(((Map) args[i]).get(key));
//                    paramList.add(key);
//                }
//                return handleParams(params, list.toArray(), paramList);
//            } else {
//                if (args[i] instanceof Serializable) {
//                    Class<?> aClass = args[i].getClass();
//                    try {
//                        aClass.getDeclaredMethod("toString", new Class[]{null});
//                        // 如果不抛出NoSuchMethodException 异常则存在 toString 方法 ，安全的writeValueAsString ，否则 走 Object的 toString方法
//                        params.append("  ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i]));
//                    } catch (NoSuchMethodException e) {
//                        params.append("  ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i].toString()));
//                    }
//                } else if (args[i] instanceof MultipartFile) {
//                    MultipartFile file = (MultipartFile) args[i];
//                    params.append("  ").append(paramNames.get(i)).append(": ").append(file.getName());
//                } else {
//                    params.append("  ").append(paramNames.get(i)).append(": ").append(args[i]);
//                }
//            }
        }
        return jsonObject.toJSONString();
    }
    @Override
    public List<SysLog> findAll() {
        return sysLogMapper.findAll();
    }

    @Override
    public int deleteById(Long id) {
        return sysLogMapper.deleteById(id);
    }
}
