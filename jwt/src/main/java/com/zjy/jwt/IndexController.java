package com.zjy.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class IndexController {

//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    public ResponseUserToken login(String username, String password) {
//        //用户验证
//        final Authentication authentication = authenticate(username, password);
//        //存储认证信息
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        //生成token
//        final User user = (User) authentication.getPrincipal();
////        User user = (User) userDetailsService.loadUserByUsername(username);
//        final String token = jwtUtils.generateToken(user);
//        //存储token
//        jwtUtils.putToken(username, token);
//        return new ResponseUserToken(token, user);
//    }
//
//    private Authentication authenticate(String username, String password) {
//        try {
//            //该方法会去调用userDetailsService.loadUserByUsername()去验证用户名和密码(User.getPassword)，如果正确，则存储该用户名密码到“security 的 context中”
//            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        } catch (DisabledException | BadCredentialsException e) {
//            throw new CustomException(ResultJson.failure(ResultCode.LOGIN_ERROR, e.getMessage()));
//        }
//    }
}
