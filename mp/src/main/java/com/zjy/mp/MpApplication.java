package com.zjy.mp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zjy.mp.mapper")
public class MpApplication {
    // https://gitee.com/baomidou/mybatisplus-spring-boot
    public static void main(String[] args) {
        SpringApplication.run(MpApplication.class, args);
    }

}
