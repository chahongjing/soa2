package com.zjy.securitycommon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注销成功后会进入此方法
 */
@Component
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setHeader("token", null);
        Cookie cookie = new Cookie(JwtTokenUtils.TOKEN_HEADER, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        cookie = new Cookie(JwtTokenUtils.TOKEN_HEADER, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setDomain("localhost");
        response.addCookie(cookie);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String reason = "注销成功";
        response.getWriter().write(new ObjectMapper().writeValueAsString(reason));
        response.flushBuffer();
    }
}
