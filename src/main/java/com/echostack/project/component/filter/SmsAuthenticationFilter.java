package com.echostack.project.component.filter;

import com.alibaba.fastjson.JSON;
import com.echostack.project.component.matcher.LoginTypeRequestMatcher;
import com.echostack.project.component.token.SmsAuthenticationToken;
import com.echostack.project.component.wapper.BodyReaderHttpServletRequestWrapper;
import com.echostack.project.infra.constant.Application;
import com.echostack.project.infra.dto.ResultGenerator;
import com.echostack.project.infra.util.JwtTokenUtil;
import com.echostack.project.model.dto.LoginResponseDto;
import com.echostack.project.model.dto.UserPasswordLoginDto;
import com.echostack.project.model.entity.SysUser;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @Author: Echo
 * @Date: 2019/4/9 11:19
 * @Description:
 */
@Data
public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private boolean postOnly = true;

    private JwtTokenUtil jwtTokenUtil;

    public SmsAuthenticationFilter() {
        super(new LoginTypeRequestMatcher("/signIn", "POST",Integer.parseInt(Application.LOGIN_TYPE_MOBILE_CODE)));
    }


    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        //从json中获取username和password
        String body = null;
        try {
            BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
            body = StreamUtils.copyToString(requestWrapper.getInputStream(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        UserPasswordLoginDto dto = new UserPasswordLoginDto();
        if(StringUtils.hasText(body)) {
            dto = JSON.parseObject(body,UserPasswordLoginDto.class);
        }

        if(dto.getUsername() == null){
            dto.setUsername("");
        }
        if(dto.getPassword() == null){
            dto.setPassword("");
        }
        //封装到token中提交
        Authentication authRequest = null;
        if(dto.getType().equals(Integer.parseInt(Application.LOGIN_TYPE_MOBILE_CODE))){
            authRequest = new SmsAuthenticationToken(dto.getUsername(),dto.getPassword());
        }

        return this.getAuthenticationManager().authenticate(authRequest);
    }
    

    protected void setDetails(HttpServletRequest request,
                              SmsAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        SysUser sysUser = (SysUser) authResult.getPrincipal();
        sysUser.setPassword("");
        String token = jwtTokenUtil.createToken(sysUser);
        jwtTokenUtil.putToken(token, sysUser.getUsername());
        System.out.println("[登录成功，token->]"+jwtTokenUtil.getTokenHead()+token);
        LoginResponseDto dto = new LoginResponseDto();
        dto.setSysUser(sysUser);
        dto.setToken(jwtTokenUtil.getTokenHead()+token);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(ResultGenerator.genSuccessResult(dto)));
    }
}
