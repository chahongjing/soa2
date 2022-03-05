package com.zjy.mongodb.dynamicDatasource;

import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;

@Slf4j
public class MultiMongoTemplate extends MongoTemplate {
    private static ThreadLocal<DatasourceType> type = ThreadLocal.withInitial(() -> DatasourceType.MASTER);

    private Map<DatasourceType, MongoDatabaseFactory> factoryMap;

    public MultiMongoTemplate(MongoDatabaseFactory primary, Map<DatasourceType, MongoDatabaseFactory> factoryMap){
        super(primary);
        this.factoryMap = factoryMap;
    }

    public static void setType(DatasourceType type) {
        MultiMongoTemplate.type.set(type);
    }
    public static DatasourceType getType() {
        return type.get();
    }
    public static void removeType() {
        MultiMongoTemplate.type.remove();
    }



    @Override
    public MongoDatabaseFactory getMongoDatabaseFactory() {
        MongoDatabaseFactory mongoDatabaseFactory = factoryMap.get(MultiMongoTemplate.getType());
        if(mongoDatabaseFactory == null) {
            return super.getMongoDatabaseFactory();
        }
        return mongoDatabaseFactory;
    }

    @Override
    public MongoDatabase getDb() {
        MongoDatabaseFactory mongoDatabaseFactory = factoryMap.get(MultiMongoTemplate.getType());
        if(mongoDatabaseFactory == null) {
            return super.getMongoDatabaseFactory().getMongoDatabase();
        }
        return super.getDb();
    }
}
