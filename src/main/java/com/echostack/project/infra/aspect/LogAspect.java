package com.echostack.project.infra.aspect;

import com.echostack.project.infra.util.IPUtil;
import com.echostack.project.model.entity.SysLog;
import com.echostack.project.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Echo
 * @Date: 2019/3/24 20:47
 * @Description:
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Value("${app.logger.isEnable}")
    private boolean isEnable;//是否启动日志功能

    @Autowired
    private SysLogService sysLogService;

    @Pointcut("@annotation(com.echostack.project.infra.annotation.Logger))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Object result = null;
        try {
            long beginTime = System.currentTimeMillis();
            // 执行方法
            result = point.proceed();
            // 获取request
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            // 设置IP地址
            String ip = IPUtil.getIpAddr(request);
            // 执行时长(毫秒)
            long time = System.currentTimeMillis() - beginTime;
            if (isEnable) {
                // 保存日志
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                SysLog log = new SysLog();
                log.setUsername(userDetails.getUsername());
                log.setIp(ip);
                log.setTime(time);
                sysLogService.add(point, log);
            }
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return result;
    }
}
