package com.zjy.mongodb.config;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "mongodb")
public class MultipleMongoProperties {

	private MongoProperties master = new MongoProperties();
	private MongoProperties slave = new MongoProperties();

	private Map<String, SimpleMongoClientDatabaseFactory> templateMuliteMap = new HashMap<>();

	public MongoProperties getMaster() {
		return master;
	}

	public void setMaster(MongoProperties master) {
		this.master = master;
	}

	public MongoProperties getSlave() {
		return slave;
	}

	public void setSlave(MongoProperties slave) {
		this.slave = slave;
	}

	public Map<String, SimpleMongoClientDatabaseFactory> getTemplateMuliteMap() {
		return this.templateMuliteMap;
	}
}
