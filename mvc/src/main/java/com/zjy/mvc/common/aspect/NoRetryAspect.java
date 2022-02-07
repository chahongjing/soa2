package com.zjy.mvc.common.aspect;

import com.alibaba.fastjson.JSON;
import com.zjy.mvc.common.NoRetry;
import com.zjy.mvc.common.component.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Slf4j
public class NoRetryAspect {
    @Resource
    private RedisUtils redisUtils;

    @Pointcut("@annotation(com.zjy.mvc.common.NoRetry)")
    public void noRetry() {
    }

    @Around(value = "noRetry()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method targetMethod = methodSignature.getMethod();
        NoRetry noRetryClass = targetMethod.getAnnotation(NoRetry.class);
        int timeout = noRetryClass.timeout();

        Object[] args = pjp.getArgs();
        String argsJson = JSON.toJSONString(args);
        String hash = DigestUtils.md5Hex(pjp.toShortString() + argsJson);
        String key = "mvc:module:op:" + hash;
        boolean r = redisUtils.lock(key, "1", timeout, TimeUnit.SECONDS);
        if (r) {
            try {
                return pjp.proceed();
            } catch (Throwable t) {
                throw t;
            } finally {
                redisUtils.del(key);
            }
        }
        throw new Exception("重复请求");
    }
}
