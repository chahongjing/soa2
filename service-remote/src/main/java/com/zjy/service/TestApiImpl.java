package com.zjy.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zjy.api.TestApi;
import com.zjy.po.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Service(interfaceClass = TestApi.class)
@Component
//@Service
public class TestApiImpl implements TestApi {

    @Autowired
    private MyUserService myUserService;
    @Autowired
    private RedisService redisService;

    @Override
    public String test() {
        redisService.set("mykey", "test");
        System.out.println("from redis: " + redisService.get("mykey"));
        MyUser myUser = myUserService.selectById(1L);
        return "zjy11";
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getName() {
        return null;
    }
}
