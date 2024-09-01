package com.zjy.shardingdatasource.config;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Collection;

//@Slf4j
@Component
public class ShardingDatabaseModuloAlgorithm implements StandardShardingAlgorithm<Long> {

    /**
     * 精确查询分片执行接口（对应的sql是where ??=值)
     *
     * @param collection:           可用的分片名集合(分库就是库名，分表就是表名)
     * @param preciseShardingValue: 分片键
     * @return 分片名
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        String logicTableName = preciseShardingValue.getLogicTableName();        //得到逻辑表名，就是sql语句的逻辑表
        //log.info("{}",logicTableName);

        Long busCode = preciseShardingValue.getValue(); //得到分片字段的值，这里分片用的是bus_code字段，系统会把这个字段传过来。
        if (busCode == null) {
            return collection.stream().findFirst().get();
        }
        return String.format("%s%d", preciseShardingValue.getDataNodeInfo().getPrefix(), busCode % 3);
//        //根据bus_code进行分片的后缀名称
//        String busType="0";
//        if(busCode.startsWith("Z0A")){
//            busType="1";
//        }else if(busCode.startsWith("S2Y")){
//            busType="2";
//        }else {
//            busType="0";
//        }
//
//        //返回实际表或者库
//        String target=logicTableName+"_"+busType;
//        if(collection.contains(target)){
//            return target;
//        }
//
//        throw new UnsupportedOperationException("route: " + busCode + " is not supported, please check your config");
//        //return null;
    }

    /***
     * 范围分片规则（对应的是where ??>='XXX' and ??<='XXX')
     * 范围查询分片算法（分片键涉及区间查询时会进入该方法进行分片计算）
     * @param collection
     * @param rangeShardingValue
     * @return 返回多个分片名
     */
    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Long> rangeShardingValue) {
        //这里返回所有的集合，也就是会到所有的表里执行语句
        return collection;
    }


    @Override
    public String getType() {
        return "SHARD_DATABASE"; //算法名称，可以自己定义。注意yaml中要用这个名字
    }
}
