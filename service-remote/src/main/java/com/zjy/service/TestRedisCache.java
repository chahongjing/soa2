package com.zjy.service;

import com.zjy.vo.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * TODO
 */
@Service
public class TestRedisCache {
    @Cacheable(cacheNames = "userKey", key = "#id")
    public User get(Long id) {
        System.out.println("get:id=" + id);
        User user = new User();
        user.setId(id);
        user.setName("zjy");
        return user;
    }

    @CachePut(cacheNames = "userKey", key = "#user.id")
    public User update(User user) {
        System.out.println("update:id=" + user.getId());
        user.setName("new zjy");
        return user;
    }

    @CacheEvict(cacheNames = "userKey")
    public void delete(Long id) {
        System.out.println("delete:id=" + id);
    }
}
