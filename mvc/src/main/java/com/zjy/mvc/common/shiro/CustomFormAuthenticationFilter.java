package com.zjy.mvc.common.shiro;

import com.alibaba.fastjson.JSON;
import com.zjy.mvc.common.BaseResult;
import com.zjy.mvc.common.ResultStatus;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            } else {
                // 放行 allow them to see the login page ;)
                return true;
            }
        } else {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            // ajax不跳转，发送一个未登录状态码, 不发送302跳转
            if (httpRequest != null && "XMLHttpRequest".equalsIgnoreCase(httpRequest.getHeader("X-Requested-With"))) {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.reset();
                httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                PrintWriter out = httpServletResponse.getWriter();
                out.print(JSON.toJSONString(new BaseResult(ResultStatus.UNAUTHENTICATION)));
                out.flush();
                out.close();
//                httpServletResponse.setStatus(HttpStatus.SC_FORBIDDEN);
//                httpServletResponse.sendError(HttpStatus.SC_FORBIDDEN);
            } else {
                saveRequestAndRedirectToLogin(request, response);
            }
            return false;
        }
    }

//    @Override
//    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
//        Subject subject = getSubject(request, response);
//        String url = getPathWithinApplication(request);
////        return subject.isPermitted(url);
//        return super.isAccessAllowed(request, response, mappedValue);
//    }
//
//    @Override
//    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
//        HttpServletRequest req =(HttpServletRequest) request;
//        HttpServletResponse resp =(HttpServletResponse) response;
//        //resp.sendRedirect(req.getContextPath());
//
//        // 返回 false 表示已经处理，例如页面跳转啥的，表示不在走以下的拦截器了（如果还有配置的话）
////        return false;
//
//        return super.onAccessDenied(request, response, mappedValue);
//    }
}