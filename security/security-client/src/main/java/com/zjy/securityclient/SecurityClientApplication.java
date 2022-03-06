package com.zjy.securityclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.zjy")
public class SecurityClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityClientApplication.class, args);
    }

}
