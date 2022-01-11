package com.zjy.esdemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zjy.esdemo.po.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * 非starter模式，直接使用api操作es
 */
@Component
@Slf4j
public class EsUtils {
    @Resource(name = "restHighLevelClientPre")
    private RestHighLevelClient restHighLevelClient;
    @Resource
    private RestClient restClient;

    // region index
    public boolean createIndex(String index) throws IOException {
        CreateIndexRequest cir = new CreateIndexRequest(index);
        cir.alias(new Alias("学生信息索引"));

        //设置分片
//        cir.settings(Settings.builder().
//                put("index.number_of_shards", 5).
//                put("index.number_of_replicas", 5).
//                put("index.max_result_window", 1000000));
//        cir.settings(settings, XContentType.JSON);
//        cir.mapping(mapping, XContentType.JSON);
        XContentBuilder builder = jsonBuilder();
        builder.startObject(); {
            builder.startObject("properties"); {
                builder.startObject("studentId"); {
                    builder.field("type", "long");
                }
                builder.endObject();
                builder.startObject("name"); {
                    builder.field("type", "keyworkd");
//                            .field("analyzer", "ik_smart")
//                            .field("search_analyzer", "ik_max_word");
                }
                builder.endObject();
                builder.startObject("age"); {
                    builder.field("type", "integer");
//                            field("analyzer", "ik_smart").
//                            field("search_analyzer", "ik_max_word");
                }
                builder.endObject();
                builder.startObject("scores"); {
                    builder.field("type", "text");
//                            field("analyzer", "ik_smart").
//                            field("search_analyzer", "ik_max_word");
                }
                builder.endObject();
                builder.startObject("birthday"); {
                    builder.field("type", "long");
//                            field("analyzer", "ik_smart").
//                            field("search_analyzer", "ik_max_word");
                }
                builder.endObject();
                builder.startObject("address"); {
                    builder.field("type", "nested");
                    builder.startObject("province"); {
                        builder.field("type", "keyword");
                    }
                    builder.endObject();
                    builder.startObject("city"); {
                        builder.field("type", "keyword");
                    }
                    builder.endObject();
                    builder.startObject("area"); {
                        builder.field("type", "keyword");
                    }
                    builder.endObject();
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();
        cir.mapping(builder);
        return restHighLevelClient.indices().create(cir, RequestOptions.DEFAULT).isAcknowledged();
    }

    public boolean indexExists(String index) {
        GetIndexRequest gir = new GetIndexRequest(index);
        boolean exists = false;
        try {
            exists = restHighLevelClient.indices().exists(gir, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public boolean deleteIndex(String index) throws IOException {
        DeleteIndexRequest dir = new DeleteIndexRequest(index);
        return restHighLevelClient.indices().delete(dir, RequestOptions.DEFAULT).isAcknowledged();
    }
    // endregion

    // region doc
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
        log.info("es search param: {}", searchRequest.source().toString());
         SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
         return searchResponse;
    }

    public boolean insertDoc(String index, String id, Student student) {
        try {
            IndexRequest ir = new IndexRequest(index);
            ir.id(id).source(XContentFactory.jsonBuilder().value(objectToJSONObject(student)));
            IndexResponse indexResponse = restHighLevelClient.index(ir, RequestOptions.DEFAULT);
            if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED || indexResponse.getResult() == DocWriteResponse.Result.NOOP) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean updateDoc(String index, String id, int age) {
        Map<String, Object> map = new HashMap<>();
        map.put("age", age);
        UpdateRequest ur = new UpdateRequest(index, id).doc(map);
//        ur.doc(JSON.toJSONString(student), XContentType.JSON);
        try {
            restHighLevelClient.update(ur, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public boolean updateDoc2(String index, String id, int age) {
        try {
            XContentBuilder content = jsonBuilder().
                    startObject().
                    field("age", age).
                    endObject();
            UpdateRequest updateRequest = new UpdateRequest(index, id).doc(content);
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED || updateResponse.getResult() == DocWriteResponse.Result.NOOP) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean deleteDoc(String index, String id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest(index, id);
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            if (deleteResponse.getResult() == DocWriteResponse.Result.DELETED) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 批量修改删除原理相同，也可混合 只需在 bulkRequest.add 不同请求即可
     * @param index 索引
     * @param list 待插入集合 注：每个String元素需为json字符串
     * @return
     */
    public BulkResponse bulkInsert(String index, List<Student> list) {
        BulkRequest bulkRequest = new BulkRequest();
        try {
        for (Student student : list) {
            IndexRequest indexRequest = new IndexRequest(index);
            indexRequest.id(student.getStudentId().toString()).source(XContentFactory.jsonBuilder().value(objectToJSONObject(student)));
//            indexRequest.id(String.valueOf(bulk.getId())).source(bulk.toString(), XContentType.JSON);
            bulkRequest.add(indexRequest); // 加入到批量请求bulk
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
         BulkResponse bulkResponse = null;
         try {
             bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
         } catch (IOException e) {
             e.printStackTrace();
         }
         return bulkResponse;
    }

    /**
     * 批量修改
     *
     * @param index
     * @param list
     */
    private void batchUpdate(String index, List<Student> list) {
        BulkRequest bulkRequest = new BulkRequest();
        list.forEach(student ->
                bulkRequest.add(new UpdateRequest(index, student.getStudentId().toString()).
                        doc(JSON.toJSONString(student), XContentType.JSON)));
        try {
            restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("es add batch data filed", e);
        }
    }

    public List<Student> getList(String index, List<String> memberIds) {
        List<Student> memberList = new ArrayList<>();

        MultiSearchRequest request = new MultiSearchRequest();
        memberIds.forEach(memberId -> {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().
                    query(QueryBuilders.matchPhraseQuery("studentId", memberId));
            request.add(new SearchRequest()
                    .source(searchSourceBuilder)
                    .indices(index));
        });
        try {
            MultiSearchResponse response = restHighLevelClient.msearch(request, RequestOptions.DEFAULT);
            for (MultiSearchResponse.Item item : response.getResponses()) {
                for (SearchHit hit : item.getResponse().getHits().getHits()) {
                    memberList.add(JSON.parseObject(hit.getSourceAsString(), Student.class));
                }
            }
        } catch (IOException e) {
            log.error("es查询异常", e);
        }
        return memberList;
    }

    /**
     * 获取所有索引别名
     * @return
     */
    public Map<String, String> getAlias() {
        GetAliasesRequest request = new GetAliasesRequest();
        Map<String, String> result = new HashMap<>();
        try {
            GetAliasesResponse getAliasesResponse = restHighLevelClient.indices().getAlias(request, RequestOptions.DEFAULT);
            Map<String, Set<AliasMetadata>> map = getAliasesResponse.getAliases();
            map.entrySet().forEach(item -> {
                result.put(item.getKey(), item.getValue().stream().map(AliasMetadata::getAlias).collect(Collectors.joining(",")));
            });
        } catch (IOException e) {
            log.error("es get indices failed", e);
        }
        return result;
    }

    // 批量处理
    public BulkProcessor init() {
        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                log.info("---尝试插入{}条数据---", request.numberOfActions());
            }
            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                log.info("---尝试插入{}条数据成功---", request.numberOfActions());
            }
            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                log.error("---尝试插入数据失败---", failure);
            }
        };
        return BulkProcessor.builder((request, bulkListener) ->
                restHighLevelClient.bulkAsync(request, RequestOptions.DEFAULT, bulkListener), listener)
                .setBulkActions(10000)
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(2)
                .build();
    }

    public void bulkIndex(String indexName, List<Student> studentList) {
        BulkProcessor bulkProcessor = init();
        IndexRequest request;
        try {
            for(Student student: studentList) {
                request = new IndexRequest(indexName);
                request.id(student.getStudentId().toString()).source(XContentFactory.jsonBuilder().value(objectToJSONObject(student)));
                bulkProcessor.add(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 嵌套查询，高亮
     * @return
     */
    public List<Student> mulitSearch() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("name", "王"))
                .must(QueryBuilders.rangeQuery("birthday").gte(new Date(2022 - 1900, 1 - 1, 4, 8, 24, 45).getTime()))
                .must(QueryBuilders.rangeQuery("birthday").lte(new Date(2022 - 1900, 1 - 1, 5, 19, 54, 32).getTime()));
        searchSourceBuilder.query(queryBuilder);

        // 嵌套查询，其中key为address.area，term为精准查询，需要设置为keyword类型，同时查询key也要添加 .keyword
//        QueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().
//                must(QueryBuilders.termQuery("address.area.keyword","南漳县"));
//        searchSourceBuilder.query(boolQueryBuilder);

        searchSourceBuilder.sort("birthday", SortOrder.ASC);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(100);
        searchSourceBuilder.timeout(new TimeValue(10, TimeUnit.SECONDS));

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(new HighlightBuilder.Field("name"));
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.fragmentSize(100);
        highlightBuilder.numOfFragments(0);
        searchSourceBuilder.highlighter(highlightBuilder);

        SearchResponse searchResponse = null;
        try {
            searchResponse = searchBySearchSourceBuilde("student_index", searchSourceBuilder);
        } catch (Exception e) {
            log.error("es search error", e);
            throw new RuntimeException(e.getMessage());
        }
        SearchHit[] hitsArr = searchResponse.getHits().getHits();
        log.info("es search took: {}", searchResponse.getTook());
        List<Student> studentList = new ArrayList<>();
        for (SearchHit hit : hitsArr) {
            Student student = JSONObject.parseObject(hit.getSourceAsString(), Student.class);
            if(!CollectionUtils.isEmpty(hit.getHighlightFields())) {
                HighlightField highlightField = hit.getHighlightFields().get("name");
                if(highlightField != null) {
                    student.setName(Arrays.stream(highlightField.getFragments()).map(Text::toString).collect(Collectors.joining("")));
                }
            }
            studentList.add(student);
        }
        return studentList;
    }

    /**
     * restclient查询es
     */
    public void test() {
        Request request = new Request("GET", "/student_index/_search");
        String jsonPath = "request.json";
        JSONReader reader = getJsonReader(jsonPath);
        request.setJsonEntity(reader.readString());
        try {
            Response response = restClient.performRequest(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode < 200 || 300 <= statusCode) {
                log.error("HTTP status code {}", response.getStatusLine().getStatusCode());
                throw new RuntimeException("HTTP status code is not 200");
            }
            String respBodyStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            JSONObject jsonObject = JSON.parseObject(respBodyStr);
        } catch (IOException e) {
            log.error("restclient request error!", e);
        }
    }

    private JSONReader getJsonReader(String srcPath) {
        JSONReader jsonReader = null;
        try {
            String resource = this.getClass().getClassLoader().getResource("").toString();
            if(resource.startsWith("file:/")) {
                resource = resource.substring(6);
            }
            FileReader fileReader=new FileReader(resource + srcPath);
            jsonReader = new JSONReader(fileReader);
        } catch (Exception e) {
            log.error("loadTestData: 读取失败");
        }
        return jsonReader;}
    // endregion

    private JSONObject objectToJSONObject(Student student) {
        JSONObject jsonObject = (JSONObject)JSON.toJSON(student);
        jsonObject.forEach((key, value) -> {
            if(value != null && value.getClass() == Date.class) {
                jsonObject.put(key, ((Date) value).getTime());
            }
        });
        jsonObject.put("_class", student.getClass().getTypeName());
        return jsonObject;
    }

    public static void main(String[] a) {
        new EsUtils().test();
    }
}