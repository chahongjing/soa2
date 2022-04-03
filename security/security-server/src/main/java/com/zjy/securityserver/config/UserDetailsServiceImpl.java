package com.zjy.securityserver.config;

import com.zjy.securitycommon.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by echisan on 2018/6/23
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 注意，此处授权要同步在JwtAuthenticationTokenFilter.doFilterInternal中调整
        List<String> authorityAndRoleList = new ArrayList<>();
        return new JwtUser(null, "zjy", bCryptPasswordEncoder.encode("123"), authorityAndRoleList);
    }

}
