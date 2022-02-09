package com.zjy.mvc.common.sql;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.*;

/**
 * @author : fei.wei
 * @Description: 解密
 * @date Date : 2020年09月03日 17:23
 */
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
      //  @Signature(type = Executor.class,method = "query",args={MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class}),
       // @Signature(type = Executor.class,method = "query",args={MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
//@Component
@Slf4j
public class MyBatisDecryptInterceptor implements Interceptor {

//    @Autowired
//    private SecurityService securityService;


    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object result = invocation.proceed();
        if (Objects.isNull(result)) {
            return null;
        }

        if (result instanceof ArrayList) {
            ArrayList resultList = (ArrayList) result;
            if (CollectionUtils.isNotEmpty(resultList) && needToDecrypt(resultList.get(0))) {
                for (int i = 0; i < resultList.size(); i++) {
                    Object object = resultList.get(i);
                    decrypt(object);
                }
            }
        } else {
            if (needToDecrypt(result)) {
                decrypt(result);
            }
        }
        return result;
    }

    private void decrypt(Object object) {
//        Class parameterObjectClass = object.getClass();
//        List<Field> declaredFields = securityService.getAllField(object);
//        declaredFields.forEach(field -> {
//            AutoEncryptOrDecrypt annotation = field.getAnnotation(AutoEncryptOrDecrypt.class);
//            if (annotation != null) {
//                String fieldToString = "";
//                String className = object.getClass().getSimpleName();
//                String fieldName = field.getName();
//                try {
//                    field.setAccessible(true);
//                    Object decryptField = field.get(object);
//                    if(decryptField != null){
//                        String decryptResult = this.securityService.decryptField(decryptField.toString());
//                        log.info("对象名：{}，字段名：{}，解密之前的值：{}，解密之后的值：{}", className, fieldName, decryptField.toString(), decryptResult);
//                        field.set(object, decryptResult);
//                    }
//                } catch (IllegalAccessException e) {
//                    log.error("解密报错==> 对象名：{}，字段名：{}，解密之前的值：{}", className, fieldName, fieldToString, e);
//                }
//            }
//        });
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * @Description: 实体是否需要解决
     * @Author: fei.wei
     * @Date: 2020/9/4 13:45
     * @param object
     * @Return:
     */
    private boolean needToDecrypt(Object object) {
//        Class<?> objectClass = object.getClass();
//        EncryptOrDecryptBean encryptDecryptClass = AnnotationUtils.findAnnotation(objectClass, EncryptOrDecryptBean.class);
//        if (Objects.nonNull(encryptDecryptClass)) {
//            return true;
//        }
        return false;
    }

}
