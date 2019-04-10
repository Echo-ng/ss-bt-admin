package com.echostack.project.component.handler;

import com.alibaba.fastjson.JSON;
import com.echostack.project.infra.dto.ResultGenerator;
import com.echostack.project.infra.util.JwtTokenUtil;
import com.echostack.project.model.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: Echo
 * @Date: 2019/3/24 23:13
 * @Description:
 */

@Component
public class AppLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String preToken = httpServletRequest.getHeader(jwtTokenUtil.getHeaderKey());
        String token = preToken.replaceAll(jwtTokenUtil.getTokenHead(),"");
        //让token失效
        jwtTokenUtil.delToken(token,((SysUser)authentication.getPrincipal()).getUsername());
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(ResultGenerator.genSuccessResult("注销成功")));
        httpServletResponse.getWriter().flush();
    }
}
