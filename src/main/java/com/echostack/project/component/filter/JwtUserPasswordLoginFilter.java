package com.echostack.project.component.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

@Data
public class JwtUserPasswordLoginFilter extends AbstractAuthenticationProcessingFilter {

    private JwtTokenUtil jwtTokenUtil;

//    private RememberMeServices rememberMeServices;

    public JwtUserPasswordLoginFilter(AuthenticationManager authenticationManager) {
        super(new LoginTypeRequestMatcher("/signIn", "POST",Integer.parseInt(Application.LOGIN_TYPE_USERNAME_PASSWORD)));
        this.setAuthenticationManager(authenticationManager);
    }

    public JwtUserPasswordLoginFilter(){
        super(new LoginTypeRequestMatcher("/signIn", "POST",Integer.parseInt(Application.LOGIN_TYPE_USERNAME_PASSWORD)));

    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        //从json中获取username和password
        String body = null;
        try {
            BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest);
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
        if(dto.getType().equals(Integer.parseInt(Application.LOGIN_TYPE_USERNAME_PASSWORD))){
            authRequest = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        }
        return this.getAuthenticationManager().authenticate(authRequest);
    }

//    public void setRememberMeServices(RememberMeServices rememberMeServices) {
//        super.setRememberMeServices(rememberMeServices);
//        this.rememberMeServices = rememberMeServices;
//    }


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
