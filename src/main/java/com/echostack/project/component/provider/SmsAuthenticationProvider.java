package com.echostack.project.component.provider;

import com.echostack.project.component.token.SmsAuthenticationToken;
import com.echostack.project.infra.constant.Application;
import com.echostack.project.infra.exception.ValidateCodeException;
import com.echostack.project.model.dto.SmsCodeDto;
import com.echostack.project.service.UserService;
import com.google.common.base.Strings;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Echo
 * @Date: 2019/4/9 11:22
 * @Description:
 */
@Data
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;

    private SessionStrategy sessionStrategy =  new HttpSessionSessionStrategy();


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        //先过滤验证码
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        ServletWebRequest webRequest = new ServletWebRequest(request);
        this.validateSmsCode(webRequest,(String)authenticationToken.getPrincipal(),(String)authenticationToken.getCredentials());
        UserDetails userDetails = userService.loadUserByMobile((String) authenticationToken.getPrincipal());
        if (userDetails == null)
            throw new UsernameNotFoundException("未找到与该手机号对应的用户");
        SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(userDetails,null, userDetails.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        //从会话中删除
        sessionStrategy.removeAttribute(webRequest,Application.SESSION_KEY_SMS_CODE + authenticationToken.getPrincipal());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return SmsAuthenticationToken.class.isAssignableFrom(aClass);
    }

    private void validateSmsCode(ServletWebRequest servletWebRequest, String mobile, String reqSmsCode) {
        SmsCodeDto smsCodeDto = (SmsCodeDto)sessionStrategy.getAttribute(servletWebRequest,Application.SESSION_KEY_SMS_CODE + mobile);
        if (Strings.isNullOrEmpty(reqSmsCode)) {
            throw new AuthenticationCredentialsNotFoundException("验证码不能为空！");
        }
        if (smsCodeDto == null) {
            throw new AuthenticationCredentialsNotFoundException("验证码不存在，请重新发送！");
        }
        if (smsCodeDto.isExpire()) {
            sessionStrategy.removeAttribute(servletWebRequest, Application.SESSION_KEY_SMS_CODE + mobile);
            throw new CredentialsExpiredException("验证码已过期，请重新发送！");
        }
        if (!StringUtils.equalsIgnoreCase(smsCodeDto.getCode(), reqSmsCode)) {
            throw new BadCredentialsException("验证码不正确！");
        }
    }
}
