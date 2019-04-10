package com.echostack.project.component.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.echostack.project.infra.util.JwtTokenUtil;
import com.echostack.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
public class JwtTokenFilter extends OncePerRequestFilter {


//    @Autowired
    private JwtTokenUtil jwtTokenUtil;

//    @Autowired
    private UserService userService;

    private SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();


    public JwtTokenFilter(){

    }

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //获取预备token，带有头前缀的
        String preToken = httpServletRequest.getHeader(jwtTokenUtil.getHeaderKey());
        if (preToken != null && preToken.startsWith(jwtTokenUtil.getTokenHead())) {
            String authToken = preToken.substring(jwtTokenUtil.getTokenHead().length());
            String username = jwtTokenUtil.parseToken(authToken).get("username").asString();
            if(jwtTokenUtil.listOnline(username).contains(authToken)) {
                if (username != null && (SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser"))) {
                    UserDetails userDetails = userService.loadUserByUsername(username);
                    try{
                        jwtTokenUtil.validate(authToken);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }catch (JWTVerificationException e) {
//                    ServletUtil.responseWriter(httpServletResponse,ResultGenerator.genFailResult(e.getMessage()));
                        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
                    }
                }
            }else{
                //token已经失效,登出
                securityContextLogoutHandler.logout(httpServletRequest,httpServletResponse,SecurityContextHolder.getContext().getAuthentication());
            }
        }else{
            //token已经失效,登出
            securityContextLogoutHandler.logout(httpServletRequest,httpServletResponse,SecurityContextHolder.getContext().getAuthentication());
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        //获取预备token，带有头前缀的
//        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
//        String preToken = httpServletRequest.getHeader(jwtTokenUtil.getHeaderKey());
//        if (preToken != null && preToken.startsWith(jwtTokenUtil.getTokenHead())) {
//            String authToken = preToken.substring(jwtTokenUtil.getTokenHead().length());
//            String username = jwtTokenUtil.parseToken(authToken).get("username").asString();
//            if(jwtTokenUtil.listOnline(username).contains(authToken)) {
//                if (username != null && (SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser"))) {
//                    UserDetails userDetails = userService.loadUserByUsername(username);
//                    try{
//                        jwtTokenUtil.validate(authToken);
//                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                    }catch (JWTVerificationException e) {
////                    ServletUtil.responseWriter(httpServletResponse,ResultGenerator.genFailResult(e.getMessage()));
//                        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
//                    }
//                }
//            }else{
//                //token已经失效,登出
//                securityContextLogoutHandler.logout(httpServletRequest,httpServletResponse,SecurityContextHolder.getContext().getAuthentication());
//            }
//        }
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//    }
}
