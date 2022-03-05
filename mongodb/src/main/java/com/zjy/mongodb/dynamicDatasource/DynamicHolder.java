package com.zjy.mongodb.dynamicDatasource;

public class DynamicHolder {
    private ThreadLocal<String> type = ThreadLocal.withInitial(() -> "master");
}
