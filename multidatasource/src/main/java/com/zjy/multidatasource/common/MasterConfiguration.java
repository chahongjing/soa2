package com.zjy.multidatasource.common;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 *
 */
@Configuration
// 指定com.zjy.daos包下的mapper都使用sqlSessionMaster数据源
@MapperScan(basePackages = {DataSourceConfiguration.MASTER_DAO_PACKAGE}, sqlSessionFactoryRef = "sqlSessionMaster")
public class MasterConfiguration {
    @Autowired
    private DataSource masterDb;

    @Bean
    @Primary
    public SqlSessionFactory sqlSessionMaster() throws Exception {
//        MybatisSqlSessionFactoryBean b = new MybatisSqlSessionFactoryBean();
//        b.setDataSource(masterDb);
//        b.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(DataSourceConfiguration.MASTER_MAPPER_LOCATION));
//        b.setTypeAliasesPackage(DataSourceConfiguration.TYPE_ALIASE);
//        b.setTypeEnumsPackage(DataSourceConfiguration.TYPE_ENUM_PACKAGE);
//        b.setVfs(SpringBootVFS.class);
//        return b.getObject();

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(masterDb);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(DataSourceConfiguration.MASTER_MAPPER_LOCATION));
        factoryBean.setTypeAliasesPackage(DataSourceConfiguration.TYPE_ALIASE);
        factoryBean.setTypeHandlersPackage(DataSourceConfiguration.TYPE_HANDLE_PACKAGE);
        factoryBean.setVfs(SpringBootVFS.class);
        return factoryBean.getObject();
    }

    @Bean
    @Primary
    public SqlSessionTemplate sqlSessionTemplateMaster() throws Exception {
        return new SqlSessionTemplate(sqlSessionMaster());
    }
}
