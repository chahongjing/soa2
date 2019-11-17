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
@MapperScan(basePackages = {"com.zjy.slavedao"}, sqlSessionFactoryRef = "sqlSessionSlave")
public class SlaveConfiguration {
    @Autowired
    @Qualifier("slaveDb")
    private DataSource slaveDb;

    @Bean
    public SqlSessionFactory sqlSessionSlave() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(slaveDb);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(DataSourceConfiguration.MAPPER_LOCATION));
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
