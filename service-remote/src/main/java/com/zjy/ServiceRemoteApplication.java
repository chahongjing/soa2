package com.zjy;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@ImportResource("classpath:*.xml")
@EnableDubboConfiguration
@MapperScan("com.zjy.dao")
@EnableCaching
public class ServiceRemoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRemoteApplication.class, args);
    }

}
