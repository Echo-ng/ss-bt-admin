package com.echostack.project.service.impl;

import com.echostack.project.infra.util.JwtTokenUtil;
import com.echostack.project.infra.util.WebUtil;
import com.echostack.project.model.entity.SysUser;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.*;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

/**
 * @Author: Echo
 * @Date: 2019/4/1 0:53
 * @Description:
 */

@Data
//@Service
public class JwtRememberMeServices extends AbstractRememberMeServices {

    private SecureRandom random = new SecureRandom();

    private int seriesLength = 16;

    @Autowired
    private PersistentTokenRepository tokenRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    protected JwtRememberMeServices(@Value("${app.remember.key}") String key,@Qualifier("userServiceImpl") UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    @Override
    protected void onLoginSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        String username = authentication.getName();
        this.logger.debug("Creating new persistent login for sysUser " + username);
        PersistentRememberMeToken persistentToken = new PersistentRememberMeToken(username, this.generateSeriesData(), this.generateTokenData(new SysUser(username)), new Date());
//        PersistentRememberMeToken persistentToken = new PersistentRememberMeToken(username, this.generateSeriesData(), "dadasdadadada", new Date());
        try {
            this.tokenRepository.createNewToken(persistentToken);
            this.addCookie(persistentToken, httpServletRequest, httpServletResponse);
        } catch (Exception var7) {
            this.logger.error("Failed to save persistent token ", var7);
        }
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws RememberMeAuthenticationException, UsernameNotFoundException {
        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain 2 tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        } else {
            String presentedSeries = cookieTokens[0];
            String presentedToken = cookieTokens[1];
            PersistentRememberMeToken token = this.tokenRepository.getTokenForSeries(presentedSeries);
            if (token == null) {
                throw new RememberMeAuthenticationException("No persistent token found for series id: " + presentedSeries);
            } else if (!presentedToken.equals(token.getTokenValue())) {
                this.tokenRepository.removeUserTokens(token.getUsername());
                throw new CookieTheftException(this.messages.getMessage("PersistentTokenBasedRememberMeServices.cookieStolen", "Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack."));
            } else if (token.getDate().getTime() + (long)this.getTokenValiditySeconds() * 1000L < System.currentTimeMillis()) {
                throw new RememberMeAuthenticationException("Remember-me login has expired");
            } else {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Refreshing persistent login token for sysUser '" + token.getUsername() + "', series '" + token.getSeries() + "'");
                }

                PersistentRememberMeToken newToken = new PersistentRememberMeToken(token.getUsername(), token.getSeries(), this.generateTokenData(new SysUser(token.getUsername())), new Date());

                try {
                    this.tokenRepository.updateToken(newToken.getSeries(), newToken.getTokenValue(), newToken.getDate());
                    this.addCookie(newToken, httpServletRequest, httpServletResponse);
                } catch (Exception var9) {
                    this.logger.error("Failed to update token: ", var9);
                    throw new RememberMeAuthenticationException("Autologin failed due to data access problem");
                }

                return this.getUserDetailsService().loadUserByUsername(token.getUsername());
            }
        }
    }

    private String generateTokenData(SysUser sysUser) {
        return jwtTokenUtil.createToken(sysUser);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        super.logout(request, response, authentication);
        if (authentication != null) {
            this.tokenRepository.removeUserTokens(authentication.getName());
        }

    }

    public void setTokenValiditySeconds(int tokenValiditySeconds) {
        Assert.isTrue(tokenValiditySeconds > 0, "tokenValiditySeconds must be positive for this implementation");
        super.setTokenValiditySeconds(tokenValiditySeconds);
    }

    private void addCookie(PersistentRememberMeToken token, HttpServletRequest request, HttpServletResponse response) {
        this.setCookie(new String[]{token.getSeries(), token.getTokenValue()}, this.getTokenValiditySeconds(), request, response);
    }

    protected String generateSeriesData() {
        byte[] newSeries = new byte[this.seriesLength];
        this.random.nextBytes(newSeries);
        return new String(Base64.getEncoder().encode(newSeries));
    }

    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        String paramValue = "";
        try {
            paramValue = WebUtil.getParamByJsonRequest(request,parameter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (paramValue != null && (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on") || paramValue.equalsIgnoreCase("yes") || paramValue.equals("1"))) {
            return true;
        } else {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Did not send remember-me cookie (principal did not set parameter '" + parameter + "')");
            }

            return false;
        }
    }
}
