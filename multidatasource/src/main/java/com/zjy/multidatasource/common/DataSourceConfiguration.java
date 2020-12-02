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
    // region master
    public final static String MASTER_DAO_PACKAGE = "com.zjy.dao";
    public final static String MASTER_MAPPER_LOCATION = "classpath:mybatis/com/zjy/dao/**.xml";
    // endregion
    // region slave
    public final static String SLAVE_DAO_PACKAGE = "com.zjy.slaveDao";
    public final static String SLAVE_MAPPER_LOCATION = "classpath:mybatis/com/zjy/slaveDao/**.xml";
    // endregion
    // 实体别名路径
    public final static String TYPE_ALIASE = MyUser.class.getPackage().getName();
    // handle处理器包路径
    public final static String TYPE_HANDLE_PACKAGE = WorkOrderType.class.getPackage().getName();
    // 处理枚举字段
//    public final static String TYPE_ENUM_PACKAGE = IBaseEnum.class.getPackage().getName();

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
