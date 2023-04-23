package com.zjy.securitycommon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    protected BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    protected LoginSuccessHandler loginSuccessHandler;
    @Autowired
    protected LoginFailureHandler loginFailureHandler;
    @Autowired
    protected LogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    protected UnAuthorizationHandler unAuthorizationHandler;
    @Autowired
    protected UnAuthenticationHandler unAuthenticationHandler;


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity
                .authorizeRequests();

//        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
//        Set<String> anonymousUrls = new HashSet<>();
//        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
//            HandlerMethod handlerMethod = infoEntry.getValue();
//            AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
//            if (null != anonymousAccess) {
//                anonymousUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
//            }
//        }

        List<String> excludeUrls = new ArrayList<>();
        excludeUrls.add("/login");
        excludeUrls.add("/login1");
        excludeUrls.add("/testPage");
        // 不需要保护的资源路径允许访问
//        for (String url : excludeUrls) {
//            registry.antMatchers(url).permitAll();
//        }
        registry.antMatchers(excludeUrls.toArray(new String[0])).permitAll();
        // 允许跨域的OPTIONS请求
        registry.antMatchers(HttpMethod.OPTIONS)
                .permitAll();
//        // 静态资源等等
//        registry.antMatchers(
//                HttpMethod.GET,
//                "/*.html",
//                "/**/*.html",
//                "/**/*.css",
//                "/**/*.js",
//                "/webSocket/**"
//        ).permitAll();
        // 其他任何请求都需要身份认证
        registry.and()
                .formLogin()
                // 指定登录页面(GET)及登录地址(表单POST)
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                // 登录成功后调用方法
                .successHandler(loginSuccessHandler)
                // 登录失败后调用方法
                .failureHandler(loginFailureHandler)
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                // 关闭跨站请求防护及不使用session
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 自定义权限拒绝处理类
                .and()
                .exceptionHandling()
                .accessDeniedHandler(unAuthorizationHandler)
                .authenticationEntryPoint(unAuthenticationHandler)
                .and()
//                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilter(new AuthenticationTokenFilter(authenticationManager())) // UsernamePasswordAuthenticationFilter
//                .addFilterBefore(new AuthenticationTokenFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
//                .httpBasic()  开启http basic验证，会创建BasicAuthenticationFilter拦截器
        ;
    }
}
