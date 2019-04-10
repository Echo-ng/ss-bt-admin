package com.echostack.project.config;

import com.echostack.project.component.filter.BodyReaderFilter;
import com.echostack.project.component.filter.JwtTokenFilter;
import com.echostack.project.component.filter.JwtUserPasswordLoginFilter;
import com.echostack.project.component.handler.AppLogoutSuccessHandler;
import com.echostack.project.component.handler.AuthFailureHandler;
import com.echostack.project.component.handler.AuthSuccessHandler;
import com.echostack.project.infra.util.JwtTokenUtil;
import com.echostack.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

/**
 * @Author: Echo
 * @Date: 2019/2/22 15:18
 * @Description:
 */
@Configuration
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Value("${app.cookie.expired}")
//    private int cookieIsExpired;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserDetailsService userDetailsService;

    @Autowired
    AuthSuccessHandler authenticationSuccessHandler;


    @Autowired
    AuthFailureHandler authenticationFailureHandler;

    @Autowired
    AppLogoutSuccessHandler appLogoutSuccessHandler;


//    @Bean
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter(jwtTokenUtil, (UserService) userDetailsService);
    }

//    @Autowired
//    JwtUserPasswordLoginFilter jwtUserPasswordLoginFilter;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

//    @Autowired
//    @Qualifier("jwtRememberMeServices")
//    private JwtRememberMeServices rememberMeServices;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BodyReaderFilter bodyReaderFilter;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
    @Bean
    protected JwtUserPasswordLoginFilter jwtUserPasswordLoginFilter(){
        JwtUserPasswordLoginFilter jwtUserPasswordLoginFilter = new JwtUserPasswordLoginFilter();
        try {
            jwtUserPasswordLoginFilter = new JwtUserPasswordLoginFilter(authenticationManager());
            jwtUserPasswordLoginFilter.setJwtTokenUtil(jwtTokenUtil);
//            jwtUserPasswordLoginFilter.setRememberMeServices(rememberMeServices);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return jwtUserPasswordLoginFilter;
    }
//    @Bean
//    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
//        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
//        // 配置数据源
//        jdbcTokenRepository.setDataSource(dataSource);
//        // 第一次启动的时候自动建表（可以不用这句话，自己手动建表，源码中有语句的）
////         jdbcTokenRepository.setCreateTableOnStartup(true);
//        return jdbcTokenRepository;
//    }

//    @Bean()
//    public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices(){
//        return new PersistentTokenBasedRememberMeServices("remember-me",userDetailsService,persistentTokenRepository());
//    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                // 设置UserDetailsService
                .userDetailsService(userDetailsService)
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() //不拦截的请求地址
                .antMatchers("/login","/signIn")
                .permitAll()
//                .antMatchers("/anonymous/**").hasRole("ANONYMOUS")
                .anyRequest().authenticated()
                .and()
                .formLogin() //表单认证
                .loginPage("/login")
                .loginProcessingUrl("/signIn")
//                .failureUrl("/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
//                .rememberMe()
//                .tokenRepository(persistentTokenRepository(dataSource))
//                .tokenValiditySeconds(cookieIsExpired)
//                .and()
                .headers().frameOptions().disable()//关闭frame防护
                .and()
                .logout() //登出配置
                .logoutUrl("/logout")
                .logoutSuccessHandler(appLogoutSuccessHandler)
                .and()
//                .anonymous().disable() //匿名登录 防护关闭
                .csrf().disable() //csrf 防护关闭
//                .addFilter(jwtUserPasswordLoginFilter())
                .addFilterBefore(bodyReaderFilter,UsernamePasswordAuthenticationFilter.class) //自定义过滤器
                .addFilterAfter(jwtTokenFilter(),UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(jwtUserPasswordLoginFilter(),UsernamePasswordAuthenticationFilter.class);
    }


}
