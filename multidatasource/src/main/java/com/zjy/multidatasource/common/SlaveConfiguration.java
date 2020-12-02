package com.zjy.multidatasource.common;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 *
 */
@Configuration
@MapperScan(basePackages = {DataSourceConfiguration.SLAVE_DAO_PACKAGE}, sqlSessionFactoryRef = "sqlSessionSlave")
public class SlaveConfiguration {
    @Autowired
    @Qualifier("slaveDb")
    private DataSource slaveDb;

    @Bean
    public SqlSessionFactory sqlSessionSlave() throws Exception {
//        MybatisSqlSessionFactoryBean b = new MybatisSqlSessionFactoryBean();
//        b.setDataSource(slaveDb);
//        b.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(DataSourceConfiguration.SLAVE_MAPPER_LOCATION));
//        b.setTypeAliasesPackage(DataSourceConfiguration.TYPE_ALIASE);
//        b.setTypeEnumsPackage(DataSourceConfiguration.TYPE_ENUM_PACKAGE);
//        b.setVfs(SpringBootVFS.class);
//        return b.getObject();

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(slaveDb);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(DataSourceConfiguration.SLAVE_MAPPER_LOCATION));
        factoryBean.setTypeAliasesPackage(DataSourceConfiguration.TYPE_ALIASE);
        factoryBean.setVfs(SpringBootVFS.class);
        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplateSlave() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionSlave());
        return template;
    }
}
