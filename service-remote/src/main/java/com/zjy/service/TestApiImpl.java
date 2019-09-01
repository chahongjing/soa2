package com.zjy.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zjy.api.TestApi;
import com.zjy.po.MyUser;
import com.zjy.vo.User;
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
    @Autowired
    private TestRedisCache testRedisCache;

    @Override
    public String test() {
        redisService.set("mykey", "test");
        Long id = 1L;
        User user = testRedisCache.get(id);
        user = testRedisCache.get(id);
        User newUser = new User();
        newUser.setId(id);
        testRedisCache.update(newUser);
        user = testRedisCache.get(id);
        user = testRedisCache.get(id);
        testRedisCache.delete(id);
        testRedisCache.get(id);
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
