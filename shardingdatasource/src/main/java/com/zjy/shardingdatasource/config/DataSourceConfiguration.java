package com.zjy.shardingdatasource.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.AlgorithmConfiguration;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.ReadwriteSplittingRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.rule.ReadwriteSplittingDataSourceRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.single.api.config.SingleRuleConfiguration;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Configuration
// 扫描dao
@MapperScan(basePackages = {"com.zjy.shardingdatasource.mapper"}, sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfiguration {
    @Resource
    private ShardingDatabaseModuloAlgorithm shardingDatabaseModuloAlgorithm;
    @Resource
    private ShardingTableModuloAlgorithm shardingTableModuloAlgorithm;

    /**
     * 配置分片数据源
     */
    @Bean
    public DataSource getShardingDataSource() throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("mvc_0", createDataSource("mvc_0"));
        dataSourceMap.put("mvc_1", createDataSource("mvc_1"));
        dataSourceMap.put("mvc_2", createDataSource("mvc_2"));

        // 分片rules规则配置
        List<RuleConfiguration> ruleConfigurationList = new ArrayList<>();
//        // 读写分离
//        ReadwriteSplittingDataSourceRuleConfiguration dataSourceConfig = new ReadwriteSplittingDataSourceRuleConfiguration(
//                "read_write_ds", "mvc_0", Arrays.asList("mvc_1", "mvc_2"), "read_strategy");
//        Properties algorithmProps = new Properties();
//        algorithmProps.setProperty("mvc_1", "2");
//        algorithmProps.setProperty("mvc_2", "1");
//        Map<String, AlgorithmConfiguration> algorithmConfigMap = new HashMap<>(1);
//        algorithmConfigMap.put("read_strategy", new AlgorithmConfiguration("WEIGHT", algorithmProps));
//        ReadwriteSplittingRuleConfiguration ruleConfig = new ReadwriteSplittingRuleConfiguration(Collections.singleton(dataSourceConfig), algorithmConfigMap);
//        ruleConfigurationList.add(ruleConfig);

        // 不分片的表
        Set<String> singleTableRule = new HashSet<>();
        singleTableRule.add("*.*");
        SingleRuleConfiguration src = new SingleRuleConfiguration();
        src.setTables(singleTableRule);
        ruleConfigurationList.add(src);

        // 配置 t_order 表分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.setShardingAlgorithms(getShardingAlgorithms());
        ShardingTableRuleConfiguration orderTableRuleConfig = new ShardingTableRuleConfiguration("user_info", "mvc_${0..2}.user_info_${0..3}");
        orderTableRuleConfig.setTableShardingStrategy(new StandardShardingStrategyConfiguration("id", shardingTableModuloAlgorithm.getType()));
        orderTableRuleConfig.setDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("id", shardingDatabaseModuloAlgorithm.getType()));
//        orderTableRuleConfig.setDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("id", "database-inline"));
        shardingRuleConfig.getTables().add(orderTableRuleConfig);
        ruleConfigurationList.add(shardingRuleConfig);

        // 是否在控制台输出解析改造后真实执行的 SQL
        Properties properties = new Properties();
        properties.setProperty("sql-show", Boolean.TRUE.toString());
        // 创建 ShardingSphere 数据源
        return ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, ruleConfigurationList, properties);
    }


    /**
     * 配置数据源
     */
    private DataSource createDataSource(String name) {
        HikariDataSource dataSource1 = new HikariDataSource();
        dataSource1.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource1.setJdbcUrl("jdbc:mysql://localhost:3306/" + name + "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&allowMultiQueries=true&rewriteBatchedStatements=true&connectTimeout=2000&socketTimeout=10000");
        dataSource1.setUsername("root");
        dataSource1.setPassword("123456");
        return dataSource1;
    }

    /**
     * 配置分片算法
     */
    private Map<String, AlgorithmConfiguration> getShardingAlgorithms() {
        Map<String, AlgorithmConfiguration> shardingAlgorithms = new LinkedHashMap<>();
        // 自定义分库算法
        Properties databaseAlgorithms = new Properties();
        databaseAlgorithms.setProperty("algorithm-expression", "mvc_$->{id % 3}");
        shardingAlgorithms.put("database-inline", new AlgorithmConfiguration("INLINE", databaseAlgorithms));
        // 自定义分表算法
        Properties tableAlgorithms = new Properties();
        tableAlgorithms.setProperty("algorithm-expression", "user_info_$->{id % 4}");
        shardingAlgorithms.put("table-inline", new AlgorithmConfiguration("INLINE", tableAlgorithms));

        Properties datasourceAlg = new Properties();
        datasourceAlg.put("strategy", "standard");
        datasourceAlg.put("algorithmClassName", ShardingDatabaseModuloAlgorithm.class.getName());
        shardingAlgorithms.put(shardingDatabaseModuloAlgorithm.getType(), new AlgorithmConfiguration("CLASS_BASED", datasourceAlg));

        Properties tableAlg = new Properties();
        tableAlg.put("strategy", "standard");
        tableAlg.put("algorithmClassName", ShardingTableModuloAlgorithm.class.getName());
        shardingAlgorithms.put(shardingTableModuloAlgorithm.getType(), new AlgorithmConfiguration("CLASS_BASED", tableAlg));
        return shardingAlgorithms;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(ResourceLoader.CLASSPATH_URL_PREFIX + "mapper/*Mapper.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 事物管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}