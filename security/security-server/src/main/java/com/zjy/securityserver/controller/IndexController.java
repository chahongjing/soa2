package com.zjy.securityserver.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public void login1(String username, String password) {
        // 获取验证码
        // 删除验证码
        // 判断验证码
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(username, password);
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌保存到redis
        // 踢除当前用户重复的令牌
    }
}
