package com.zjy.mvc.common.multiDataSource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author yehui
 * 多数据源的选择
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        log.info("切换到{" + DynamicDataSourceContextHolder.getDB() + "}数据源");
        return DynamicDataSourceContextHolder.getDB();
    }
}