package com.zjy.securityserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjy.securitycommon.JwtTokenUtils;
import com.zjy.securityserver.LoginUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by echisan on 2018/6/23
 */

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private ThreadLocal<Integer> rememberMe = new ThreadLocal<>();
    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
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
        try {
            LoginUser loginUser = new ObjectMapper().readValue(request.getInputStream(), LoginUser.class);
            rememberMe.set(loginUser.getRememberMe() == null ? 0 : loginUser.getRememberMe());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 成功验证后调用的方法
    // 如果验证成功，就生成token并返回
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        System.out.println("jwtUser:" + jwtUser.toString());
        boolean isRemember = rememberMe.get() == 1;

        String role = "";
        Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();
        for (GrantedAuthority authority : authorities){
            role = authority.getAuthority();
        }

        String token = JwtTokenUtils.createToken(jwtUser.getUsername(), role, isRemember);
//        String token = JwtTokenUtils.createToken(jwtUser.getUsername(), false);
        // 返回创建成功的token
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的时候应该是 `Bearer token`
        response.setHeader("token", JwtTokenUtils.TOKEN_PREFIX + token);
        Cookie cookie = new Cookie(JwtTokenUtils.TOKEN_HEADER, token);
        cookie.setPath("/");
        response.addCookie(cookie);

        cookie = new Cookie(JwtTokenUtils.TOKEN_HEADER, token);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);

        String redirectUrl = request.getParameter("redirectUrl");
        if(StringUtils.isEmpty(redirectUrl)) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            String reason = "登录成功";
            response.getWriter().write(new ObjectMapper().writeValueAsString(reason));
            response.flushBuffer();
        } else {
            response.sendRedirect(redirectUrl);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);

        Map<String, String> result = new HashMap<>();
        result.put("msg", "登录失败：" + failed.getMessage());
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}
