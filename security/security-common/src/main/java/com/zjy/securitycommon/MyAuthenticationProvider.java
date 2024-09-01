package com.zjy.securitycommon;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

//Component
public class MyAuthenticationProvider implements AuthenticationProvider {
//    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //check from db
//        String userName = authentication.getName();
//        String pwd = authentication.getCredentials().toString();
//        UserInfo要实现UserDetails
//        UserInfo user = userService.loadUserByUserName(userName);
//        if (bCryptPasswordEncoder.matches(pwd, user.getPwd())) {
//            return new UsernamePasswordAuthenticationToken(userName, pwd, user.getAuthorities());
//        } else {
//            throw new BadCredentialsException("用户名和密码错误");
//        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 未验证
        return false;
    }
}
