package com.zjy.securitycommon;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BaseConfig {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        bCryptPasswordEncoder.matches(oldPass, user.getPassword())
        return new BCryptPasswordEncoder();
    }
}
