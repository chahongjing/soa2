package com.zjy.mvc.common.sql;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author : fei.wei
 * @Description: 加密
 * @date Date : 2020年09月03日 16:48
 */
@Intercepts({
        @Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class)
       // @Signature(type = Executor.class,method = "update",args={MappedStatement.class,Object.class})

})
//@Component
@Slf4j
public class MyBatisEncryptInterceptor implements Interceptor {
//    @Autowired
//    private SecurityService securityService;
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //拦截 ParameterHandler 的 setParameters 方法 动态设置参数
//        if (invocation.getTarget() instanceof ParameterHandler) {
//            ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
//
//            // 反射获取 参数对像
//            Field parameterField =
//                    parameterHandler.getClass().getDeclaredField("parameterObject");
//            parameterField.setAccessible(true);
//            Object parameterObject = parameterField.get(parameterHandler);
//            if (Objects.nonNull(parameterObject)){
//                Class<?> parameterObjectClass = parameterObject.getClass();
//                EncryptOrDecryptBean encryptDecryptClass = AnnotationUtils.findAnnotation(parameterObjectClass, EncryptOrDecryptBean.class);
//                if (Objects.nonNull(encryptDecryptClass)){
//
//                    List<Field> declaredFields = this.securityService.getAllField(parameterObject);
//                    // 加密
//                    declaredFields.forEach(field -> {
//                        AutoEncryptOrDecrypt annotation = field.getAnnotation(AutoEncryptOrDecrypt.class);
//                        if(annotation != null){
//                            String fieldToString = "";
//                            String className = parameterObject.getClass().getSimpleName();
//                            String fieldName = field.getName();
//                            try {
//                                field.setAccessible(true);
//                                Object encryptField = field.get(parameterObject);
//                                if(encryptField != null){
//                                    String encryptResult = this.securityService.encryptField(encryptField.toString());
//                                    field.set(parameterObject,encryptResult);
//                                    log.info("对象名：{}，字段名：{}，加密之前的值：{}，加密之后的值：{}", className, fieldName, encryptField.toString(), encryptResult);
//                                }
//                            } catch (Exception e) {
//                               log.error("加密报错==> 对象名：{}，字段名：{}，加密之前的值：{}", className, fieldName, fieldToString, e);
//                            }
//                        }
//                    });
//                }
//            }
//        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
