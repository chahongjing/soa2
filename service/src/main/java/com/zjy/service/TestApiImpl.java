package com.zjy.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zjy.api.TestApi;
import com.zjy.dao.UserDao;
import com.zjy.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO
 */
@Service(interfaceClass = TestApi.class)
@Component
//@Service
public class TestApiImpl implements TestApi {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisService redisService;

    @Override
    public String test() {
        redisService.set("mykey", "test");
        System.out.println("from redis: " + redisService.get("mykey"));
        User user = userDao.get(1L);
        return "zjy11";
    }
}
