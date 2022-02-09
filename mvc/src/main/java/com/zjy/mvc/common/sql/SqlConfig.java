package com.zjy.mvc.common.sql;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class SqlConfig implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes sqlPrint = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableSqlPrint.class.getName()));
        if (sqlPrint == null) {
            return;
        }
        boolean defaultPrintSql = sqlPrint.getBoolean("defaultPrint");
        BeanDefinitionBuilder sqlPrintBeanDefinition = BeanDefinitionBuilder.rootBeanDefinition(SqlPrint.class);
        sqlPrintBeanDefinition.addPropertyValue("defaultPrint", defaultPrintSql);
        registry.registerBeanDefinition(SqlPrint.class.getName(), sqlPrintBeanDefinition.getBeanDefinition());
    }
}