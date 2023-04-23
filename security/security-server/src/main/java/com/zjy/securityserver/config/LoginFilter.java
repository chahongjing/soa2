package com.zjy.securityserver.config;

import com.zjy.securitycommon.LoginFailureHandler;
import com.zjy.securitycommon.LoginSuccessHandler;
import com.zjy.securityserver.LoginUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * 默认使用 UsernamePasswordAuthenticationFilter，基本满足需求，也可以订制
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {


    public LoginFilter(AuthenticationManager authenticationManager,
                       LoginSuccessHandler loginSuccessHandler,
                       LoginFailureHandler loginFailureHandler) {
        this.setAuthenticationManager(authenticationManager);
        this.setAuthenticationSuccessHandler(loginSuccessHandler);
        this.setAuthenticationFailureHandler(loginFailureHandler);
        // 设置拦截的请求路径，访问此路径时会进入attemptAuthentication方法
        super.setFilterProcessesUrl("/auth/login");
    }

    /**
     * 访问上面设置的路径后会调用此方法，目前主要用来处理登录，获取用户名和密码，返回Authentication
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        // 从输入流中获取到登录的信息
//            LoginUser loginUser = new ObjectMapper().readValue(request.getInputStream(), LoginUser.class);
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(request.getParameter("username"));
        loginUser.setPassword(request.getParameter("password"));
        return this.getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword(), new ArrayList<>())
        );
    }
}
