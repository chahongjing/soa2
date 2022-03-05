package com.zjy.mongodb.config;

import com.zjy.mongodb.dynamicDatasource.DatasourceType;
import com.zjy.mongodb.dynamicDatasource.MultiMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultipleMongoConfig {
//	单节点直接使用如下
//	dao extends MongoRepository<>
//	MongoTemplate

	@Autowired
    private MultipleMongoProperties mongoProperties;

	@Primary
	@Bean(name = MasterMongoConfig.MONGO_TEMPLATE)
	public MongoTemplate masterMongoTemplate() {
		MongoDatabaseFactory master = masterFactory();
		MongoDatabaseFactory slave = slaveFactory();
		Map<DatasourceType, MongoDatabaseFactory> factoryMap = new HashMap<>();
		factoryMap.put(DatasourceType.MASTER, master);
		factoryMap.put(DatasourceType.SLAVE, slave);
		return new MultiMongoTemplate(master, factoryMap);
	}

	@Bean
	@Qualifier(SlaveMongoConfig.MONGO_TEMPLATE)
	public MongoTemplate slaveMongoTemplate() {
		return new MongoTemplate(slaveFactory());
	}

	@Bean
	@Primary
	public MongoDatabaseFactory masterFactory()  {
		SimpleMongoClientDatabaseFactory master = new SimpleMongoClientDatabaseFactory(mongoProperties.getMaster().getUri());
		mongoProperties.getTemplateMuliteMap().put("master", master);
		return master;
	}
	@Bean
	public MongoDatabaseFactory slaveFactory() {
		SimpleMongoClientDatabaseFactory slave = new SimpleMongoClientDatabaseFactory(mongoProperties.getSlave().getUri());
		mongoProperties.getTemplateMuliteMap().put("slave", slave);
		return slave;
	}
}