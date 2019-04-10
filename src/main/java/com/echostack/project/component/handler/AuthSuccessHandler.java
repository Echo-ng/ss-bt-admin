package com.echostack.project.component.handler;

import com.echostack.project.infra.util.JwtTokenUtil;
import com.echostack.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${jwt.header.key}")
    private String headerKey;
    @Value("${jwt.token.head}")
    private String tokenHead;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

//    @Autowired
//    private RedisTemplate redisTemplate;

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        SavedRequest savedRequest = requestCache.getRequest(httpServletRequest, httpServletResponse);
//        SysUser sysUser = (SysUser) authentication.getPrincipal();
//        String token = jwtTokenUtil.createToken(sysUser);
//        jwtTokenUtil.putToken(token,sysUser.getUsername());
//        System.out.println("[登录成功，token->]"+jwtTokenUtil.getTokenHead()+token);
//        httpServletResponse.setHeader(headerKey,tokenHead + token);
        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, savedRequest.getRedirectUrl());
    }
}
