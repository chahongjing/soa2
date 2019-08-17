package com.zjy;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ImportResource("classpath:*.xml")
@EnableDubboConfiguration
@MapperScan("com.zjy.dao")
public class ServiceRemoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRemoteApplication.class, args);
    }

}
