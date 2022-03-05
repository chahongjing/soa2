package com.zjy.mongodb.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@Configuration
@EnableConfigurationProperties(MultipleMongoProperties.class)
@EnableMongoRepositories(basePackages = "com.zjy.mongodb.master",
		mongoTemplateRef = "masterMongoTemplate")
public class MasterMongoConfig {
	public final static String MONGO_TEMPLATE = "masterMongoTemplate";
}
