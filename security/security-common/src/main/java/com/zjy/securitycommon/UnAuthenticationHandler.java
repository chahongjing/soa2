package com.zjy.securitycommon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户未登录，访问需要登录的页面时会进入此方法
 *
 * @description:没有携带token或者token无效
 */
@Component
public class UnAuthenticationHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        Map<String, String> result = new HashMap<>();
        result.put("msg", "未登录：" + authException.getMessage());
        result.put("data", "http://localhost:8102/login");
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));

//        StringBuffer requestURL = request.getRequestURL();
//        response.sendRedirect();
    }
}
