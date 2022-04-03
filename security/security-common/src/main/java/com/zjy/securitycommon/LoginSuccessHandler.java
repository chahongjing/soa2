package com.zjy.securitycommon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * 登录成功后操作
 */
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        System.out.println("jwtUser:" + jwtUser.toString());
//        boolean isRemember = rememberMe.get() == 1;
        boolean isRemember = false;

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
}
