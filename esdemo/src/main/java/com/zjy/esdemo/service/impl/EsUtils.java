package com.zjy.esdemo.service.impl;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 非starter模式，直接使用api操作es
 */
@Component
public class EsUtils {
    @Resource(name = "restHighLevelClientPre")
    private RestHighLevelClient restHighLevelClient;

    /**
     * 查询
     * @param index 索引
     * @param searchSourceBuilder
     * @return
     * @throws IOException
     */
    public SearchResponse searchBySearchSourceBuilde(String index, SearchSourceBuilder searchSourceBuilder) throws IOException {
        // 组装SearchRequest请求
         SearchRequest searchRequest = new SearchRequest(index);
         searchRequest.source(searchSourceBuilder);
        // 同步获取SearchResponse结果
         SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
         return searchResponse;
    }

    public boolean deleteIndex(String index) throws IOException {
        DeleteIndexRequest dir = new DeleteIndexRequest(index);
        return restHighLevelClient.indices().delete(dir, RequestOptions.DEFAULT).isAcknowledged();
    }

    /**
     * 批量插入  批量修改删除原理相同，也可混合 只需在 bulkRequest.add 不同请求即可
     * @param jsonStrList 待插入集合 注：每个String元素需为json字符串
     * @param index 索引
     * @return
     */
    public BulkResponse bulkInsert(List<String> jsonStrList, String index) {
        BulkRequest bulkRequest = new BulkRequest();
        for (String jsonStr : jsonStrList) {
            IndexRequest indexRequest = new IndexRequest(index);
            indexRequest.source(jsonStr, XContentType.JSON);
            bulkRequest.add(indexRequest); // 加入到批量请求bulk
        }
         BulkResponse bulkResponse = null;
         try {
             bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
         } catch (IOException e) {
             e.printStackTrace();
         }
         return bulkResponse;
    }
}