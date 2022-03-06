package com.zjy.securitycommon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by echisan on 2018/6/24
 *
 * @description:没有携带token或者token无效
 */
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
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
