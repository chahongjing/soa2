package com.zjy.mvc.common.component;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    @Resource
    @Lazy
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 加锁
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     * @return
     */
    public boolean lock(String key, String value, int timeout, TimeUnit timeUnit) {
        Boolean r = stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
        return r != null && r;
    }

    public boolean del(String key) {
        Boolean r = stringRedisTemplate.delete(key);
        return r == null || r;
    }
}
