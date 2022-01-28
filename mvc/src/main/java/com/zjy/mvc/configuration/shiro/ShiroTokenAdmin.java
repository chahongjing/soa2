package com.zjy.mvc.configuration.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class ShiroTokenAdmin extends UsernamePasswordToken {
    public ShiroTokenAdmin(String username, String password) {
        super(username, password);
    }
}
