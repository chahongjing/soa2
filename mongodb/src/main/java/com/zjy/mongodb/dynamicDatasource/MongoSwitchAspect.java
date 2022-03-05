package com.zjy.mongodb.dynamicDatasource;

import com.zjy.mongodb.config.MultipleMongoProperties;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Aspect
public class MongoSwitchAspect {
    @Autowired
    private MultipleMongoProperties mongoProperties;

    @Pointcut("@annotation(DynamicType)")
    public void routeMongoDB() {

    }

    @Around("routeMongoDB()")
    public Object routeMongoDB(ProceedingJoinPoint joinPoint) {
        Object result = null;
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method targetMethod = methodSignature.getMethod();
        DynamicType type = targetMethod.getAnnotation(DynamicType.class);
        if(type != null && type.type() == DatasourceType.SLAVE) {
            MultiMongoTemplate.setType(DatasourceType.SLAVE);
        } else {
            MultiMongoTemplate.setType(DatasourceType.MASTER);
        }
        try {
            result = joinPoint.proceed();
        } catch (Throwable t) {
            log.error("", t);
        } finally {
            MultiMongoTemplate.removeType();
        }
        return result;
    }
}
