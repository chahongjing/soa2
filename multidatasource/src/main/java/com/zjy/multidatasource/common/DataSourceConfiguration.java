package com.zjy.multidatasource.common;

import com.zjy.enums.WorkOrderType;
import com.zjy.po.MyUser;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 *
 */
@Configuration
public class DataSourceConfiguration {
    public final static String MAPPER_LOCATION = "classpath:mybatis/com/zjy/dao/**.xml";
    public final static String TYPE_ALIASE = MyUser.class.getPackage().getName();
    public final static String TYPE_HANDLE_PACKAGE = WorkOrderType.class.getPackage().getName();


    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDb() {
        DataSource ds = DataSourceBuilder.create().build();
        return ds;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDb() {
        return DataSourceBuilder.create().build();
    }
}
