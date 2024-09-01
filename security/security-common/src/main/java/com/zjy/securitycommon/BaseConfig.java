package com.zjy.securitycommon;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BaseConfig {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//        bCryptPasswordEncoder.matches(oldPass, user.getPassword())
        return new BCryptPasswordEncoder();
    }
}
