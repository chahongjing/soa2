package com.zjy.web.common;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * TODO
 */
@Component
@Order(value = 1)
public class MyStartupRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作<<<<<<<<<<<<<");
    }
}
