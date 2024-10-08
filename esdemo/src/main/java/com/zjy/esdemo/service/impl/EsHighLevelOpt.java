package com.zjy.esdemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zjy.esdemo.po.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.mapper.CompletionFieldMapper;
import org.elasticsearch.index.mapper.KeywordFieldMapper;
import org.elasticsearch.index.mapper.NestedObjectMapper;
import org.elasticsearch.index.mapper.NumberFieldMapper;
import org.elasticsearch.index.mapper.ObjectMapper;
import org.elasticsearch.index.mapper.TextFieldMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.zjy.esdemo.service.impl.Constants.STUDENT_INDEX;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * 非starter模式，直接使用highleve api操作es
 */
@Component
@Slf4j
public class EsHighLevelOpt {
    @Resource
    private RestHighLevelClient restHighLevelClient;

    // region index
    public boolean createIndex(String index) throws IOException {
        if (indexExists(index)) {
            return false;
        }
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
        builder.startObject();
        {
            builder.startObject("properties");
            {
                builder.startObject("_class");
                {
                    builder.field("type", KeywordFieldMapper.CONTENT_TYPE);
                }
                builder.endObject();
                builder.startObject("studentId");
                {
                    builder.field("type", KeywordFieldMapper.CONTENT_TYPE);
                }
                builder.endObject();
                builder.startObject("name");
                {
                    builder.field("type", KeywordFieldMapper.CONTENT_TYPE);
                }
                builder.endObject();
                builder.startObject("name_completion");
                {
                    builder.field("type", CompletionFieldMapper.CONTENT_TYPE);
                }
                builder.endObject();
                builder.startObject("desc");
                {
                    builder.field("type", TextFieldMapper.CONTENT_TYPE);
//                            .field("analyzer", "ik_smart")
//                            .field("search_analyzer", "ik_max_word");
                }
                builder.endObject();
                builder.startObject("age");
                {
                    builder.field("type", NumberFieldMapper.NumberType.INTEGER.typeName());
                }
                builder.endObject();
                builder.startObject("scores");
                {
                    builder.field("type", NumberFieldMapper.NumberType.DOUBLE.typeName());
//                            field("analyzer", "ik_smart").
//                            field("search_analyzer", "ik_max_word");
                }
                builder.endObject();
                builder.startObject("birthday");
                {
                    builder.field("type", NumberFieldMapper.NumberType.LONG.typeName());
                }
                builder.endObject();
                builder.startObject("address");
                {
                    builder.field("type", ObjectMapper.CONTENT_TYPE);

                    builder.startObject("properties");
                    {
                        builder.startObject("_class");
                        {
                            builder.field("type", KeywordFieldMapper.CONTENT_TYPE);
                        }
                        builder.endObject();

                        builder.startObject("province");
                        {
                            builder.field("type", KeywordFieldMapper.CONTENT_TYPE);
                        }
                        builder.endObject();
                        builder.startObject("city");
                        {
                            builder.field("type", KeywordFieldMapper.CONTENT_TYPE);
                        }
                        builder.endObject();
                        builder.startObject("area");
                        {
                            builder.field("type", KeywordFieldMapper.CONTENT_TYPE);
                        }
                        builder.endObject();
                    }
                    builder.endObject();
                }
                builder.endObject();
                builder.startObject("interests");
                {
                    builder.field("type", NestedObjectMapper.CONTENT_TYPE);
                    builder.startObject("properties");
                    {
                        builder.startObject("_class");
                        {
                            builder.field("type", KeywordFieldMapper.CONTENT_TYPE);
                        }
                        builder.endObject();
                        builder.startObject("code");
                        {
                            builder.field("type", KeywordFieldMapper.CONTENT_TYPE);
                        }
                        builder.endObject();
                        builder.startObject("name");
                        {
                            builder.field("type", KeywordFieldMapper.CONTENT_TYPE);
                        }
                        builder.endObject();
                    }
                    builder.endObject();
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();
        cir.mapping(builder);
//        cir.mapping(Utils.getJsonByTemplate("mapping.json"), XContentType.JSON);
        log.info("mapping:{}", JSON.toJSONString(builder));
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
        if (indexExists(index)) {
            DeleteIndexRequest dir = new DeleteIndexRequest(index);
            return restHighLevelClient.indices().delete(dir, RequestOptions.DEFAULT).isAcknowledged();
        }
        return false;
    }
    // endregion

    // region doc

    /**
     * 查询
     *
     * @param index               索引
     * @param searchSourceBuilder
     * @return
     * @throws IOException
     */
    public SearchResponse searchBySearchSourceBuilder(String index, SearchSourceBuilder searchSourceBuilder) {
        // 组装SearchRequest请求
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(searchSourceBuilder);
        // 同步获取SearchResponse结果
        log.info("es search param: {}", searchRequest.source().toString());
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("es search error!", e);
        }
        return searchResponse;
    }

    public boolean insertDoc(String index, String id, Student student) {
        try {
//            Requests.createIndexRequest(index);
            IndexRequest ir = new IndexRequest(index);
            ir.id(id).source(XContentFactory.jsonBuilder().value(objectToJSONObject(student)));
//            ir.id(id).timeout("1s").source(JSON.toJSON(student), XContentType.JSON);
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

    public boolean docExists(String index, String id) {
        GetRequest gr = new GetRequest(index, id);
        boolean exists = false;
        try {
            gr.fetchSourceContext(new FetchSourceContext(false));
            gr.storedFields("_none_");
            exists = restHighLevelClient.exists(gr, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
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
            // docAsUpsert
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

    public long deleteByQuery() {
        //参数为索引名，可以不指定，可以一个，可以多个
        DeleteByQueryRequest request = new DeleteByQueryRequest(STUDENT_INDEX);
        // 更新时版本冲突
        request.setConflicts("proceed");
        // 设置查询条件，第一个参数是字段名，第二个参数是字段的值
        request.setQuery(new TermQueryBuilder("name", "黄金"));
        // 更新最大文档数
        request.setSize(10);
        // 批次大小
        request.setBatchSize(1000);
        // 并行
        request.setSlices(2);
        // 使用滚动参数来控制“搜索上下文”存活的时间
        request.setScroll(TimeValue.timeValueMinutes(10));
        // 超时
        request.setTimeout(TimeValue.timeValueMinutes(2));
        // 刷新索引
        request.setRefresh(true);
        try {
            BulkByScrollResponse response = restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
            return response.getStatus().getUpdated();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    /**
     * 批量修改删除原理相同，也可混合 只需在 bulkRequest.add 不同请求即可
     *
     * @param index 索引
     * @param list  待插入集合 注：每个String元素需为json字符串
     * @return
     */
    public BulkResponse bulkInsert(String index, List<Student> list) {
        BulkRequest bulkRequest = new BulkRequest();
        BulkResponse bulkResponse = null;
        try {
            for (Student student : list) {
                IndexRequest indexRequest = new IndexRequest(index);
                indexRequest.id(student.getStudentId().toString()).source(XContentFactory.jsonBuilder().value(objectToJSONObject(student)));
//                indexRequest.id(student.getStudentId().toString()).source(XContentFactory.jsonBuilder().value(objectToJSONObject(student)), XContentType.JSON);
                //            indexRequest.id(String.valueOf(bulk.getId())).source(bulk.toString(), XContentType.JSON);
                bulkRequest.add(indexRequest); // 加入到批量请求bulk
            }
            log.info("bulk request. param: {}", bulkRequest.requests());
            bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            boolean b = bulkResponse.hasFailures();
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

    public void updateByQueryRequest() throws IOException {
        UpdateByQueryRequest request = new UpdateByQueryRequest(STUDENT_INDEX);
        request.setConflicts("proceed");
        request.setQuery(new TermQueryBuilder("name", "王"));
        request.setSize(2);
        request.setScript(new Script(ScriptType.INLINE,
                "painless",
                "if (ctx._source.bankType == 'BOC') {ctx._source.aliasName='hello'}",
                Collections.emptyMap())
        );
        BulkByScrollResponse resp = restHighLevelClient.updateByQuery(request, RequestOptions.DEFAULT);
        resp.getTotal();
        resp.getUpdated();
        resp.getDeleted();
    }

    public Student get(Long id) {
        GetRequest gr = new GetRequest(STUDENT_INDEX, id.toString());
        try {
            GetResponse response = restHighLevelClient.get(gr, RequestOptions.DEFAULT);
            return JSON.parseObject(response.getSourceAsBytes(), Student.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取所有索引别名
     *
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

    public void bulkInsertWithProcessor(String indexName, List<Student> studentList) {
        BulkProcessor bulkProcessor = init();
        IndexRequest request;
        try {
            for (Student student : studentList) {
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
     *
     * @return
     */
    public List<Student> mulitSearch() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        Calendar myCalendar = new GregorianCalendar(2022, Calendar.JANUARY, 4, 8, 24, 45);

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("name", QueryParserBase.escape("王")))
                .must(QueryBuilders.rangeQuery("birthday").gte(myCalendar.getTime()))
                .must(QueryBuilders.rangeQuery("birthday").lte(new Date(2022 - 1900, Calendar.JANUARY, 5, 19, 54, 32).getTime()));
        searchSourceBuilder.query(queryBuilder);

        searchSourceBuilder.sort("birthday", SortOrder.ASC);
//        searchSourceBuilder.sort(SortBuilders.scoreSort().order(SortOrder.ASC));
//        searchSourceBuilder.sort(SortBuilders.fieldSort("birthday").order(SortOrder.ASC));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(100);
        searchSourceBuilder.timeout(new TimeValue(10, TimeUnit.SECONDS));
        // 会返回所有命中的条数，默认只会返回10000
        searchSourceBuilder.trackTotalHits(true);

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(new HighlightBuilder.Field("name"));
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.fragmentSize(100);
        highlightBuilder.numOfFragments(0);
        searchSourceBuilder.highlighter(highlightBuilder);

        List<Student> studentList = new ArrayList<>();
        SearchResponse searchResponse = searchBySearchSourceBuilder(STUDENT_INDEX, searchSourceBuilder);
        if (searchResponse == null) {
            return studentList;
        }

        SearchHit[] hitsArr = searchResponse.getHits().getHits();
        log.info("es search took: {}", searchResponse.getTook());
        for (SearchHit hit : hitsArr) {
            Student student = JSONObject.parseObject(hit.getSourceAsString(), Student.class);
            if (!CollectionUtils.isEmpty(hit.getHighlightFields())) {
                HighlightField highlightField = hit.getHighlightFields().get("name");
                if (highlightField != null) {
                    student.setName(Arrays.stream(highlightField.getFragments()).map(Text::toString).collect(Collectors.joining("")));
                }
            }
            studentList.add(student);
        }
        return studentList;
    }

    /**
     * object搜索
     * object直接用.查询，如address.area。注意如果索引类型是nested，此操作无法查出数据
     * nested要用nested嵌套查询，注意path
     *
     * @param index
     */
    public void searchInObject(String index) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().
                must(QueryBuilders.termQuery("address.area", "南漳县"));
        searchSourceBuilder.query(boolQueryBuilder);

        SearchResponse response = searchBySearchSourceBuilder(index, searchSourceBuilder);
        System.out.println(response.toString());
        response.getHits().forEach(hi -> {
            System.out.println(hi.getSourceAsString());
        });
    }

    /**
     * nest搜索。注意如果索引类型不是nested，此操作无法查出数据
     *
     * @param index
     */
    public void searchNested(String index) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        NestedQueryBuilder nested = QueryBuilders.nestedQuery("interests",
                QueryBuilders.matchQuery("interests.name", "刷视频"),
                ScoreMode.None);
        searchSourceBuilder.query(nested);

        SearchResponse response = searchBySearchSourceBuilder(index, searchSourceBuilder);
        System.out.println(response.toString());
        response.getHits().forEach(hi -> {
            System.out.println(hi.getSourceAsString());
        });
    }

    public List<Student> search(String index, Long region, String key) throws IOException {
        if (StringUtils.isBlank(key)) return new ArrayList<>();
        key = key.trim();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        builder.query(boolQueryBuilder);

        BoolQueryBuilder filterQueryBuilder = new BoolQueryBuilder();
        filterQueryBuilder.must(QueryBuilders.termQuery("region", region));
        boolQueryBuilder.filter(filterQueryBuilder);
        boolQueryBuilder.must(QueryBuilders.matchQuery("query", key).operator(Operator.AND));
        builder.from(0);
        builder.size(0);

        // 按关键字分组后，把每个文档的分数加起来，按分数倒序排列
        TermsAggregationBuilder aggre = AggregationBuilders
                .terms("query_keyword")
                .field("query_keyword")
                .size(10)
                // 按分数倒排
                .order(BucketOrder.aggregation("su", false));
        // 计算分数脚本
        aggre.subAggregation(AggregationBuilders.sum("su").script(new Script("_score")));
        builder.aggregation(aggre);
        log.info("query search builder: {}, agg:{}", builder, aggre);
        SearchRequest request = new SearchRequest(index);
        request.source(builder);
        final SearchResponse search = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        if (search == null || search.getHits() == null || ArrayUtils.isEmpty(search.getHits().getHits()))
            return new ArrayList<>();
        List<Student> list = new ArrayList<>();
        Set<String> querySet = new HashSet<>();
        for (SearchHit hit : search.getHits()) {
            final Student bean = JSON.parseObject(hit.getSourceAsString(), Student.class);
            if (key.equals(bean.getName()) || list.size() >= 10 || querySet.contains(bean.getName())) continue;
            querySet.add(bean.getName());
            list.add(bean);
        }
        return list;
    }

    /**
     * 也可以使用 searchafter 避免深度分页，searchafter在执行过程中，有可能排序的值发生变化，导致有重复或缺失。
     *
     * @param indexName
     * @return
     */
    public void scrollFetch(String indexName, QueryBuilder queryBuilder, Consumer<SearchHit[]> consumer) {
        int pageSize = 100;
        SearchRequest request = new SearchRequest(indexName);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        request.source(builder);
        builder.query(queryBuilder);
        builder.size(pageSize);
        builder.fetchSource(new String[]{"id"}, Strings.EMPTY_ARRAY);

        Scroll scroll = new Scroll(TimeValue.timeValueMinutes(5L));
        request.scroll(scroll);

        SearchResponse response = null;
        int count = 1;
        try {
            log.info("scroll param: {}", builder);
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            log.info("begin fetch. {}", count);
        } catch (IOException e) {
            log.error("查询es出错", e);
        }
        if (response == null) {
            return;
        }
        SearchHits hits = response.getHits();
        //记录要滚动的ID
        String scrollId = response.getScrollId();
        SearchHit[] hitsScroll = hits.getHits();
        while (hitsScroll != null && hitsScroll.length > 0) {
            consumer.accept(hitsScroll);

            // 再构造滚动查询条件，进行下一次查询
            SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
            searchScrollRequest.scroll(scroll);
            try {
                //响应必须是上面的响应对象，需要对上一层进行覆盖
                response = restHighLevelClient.scroll(searchScrollRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                log.error("滚动查询失败", e);
            }
            if (response == null) {
                return;
            }
            scrollId = response.getScrollId();
            hits = response.getHits();
            hitsScroll = hits.getHits();
            if (hitsScroll != null && hitsScroll.length > 0) {
                count++;
                log.info("next fetch. {}", count);
            }
        }
        log.info("fetch es done. {}", count);
    }

    public void testSearchAfter(String indexName) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //排序条件
        searchSourceBuilder.sort("id", SortOrder.ASC);
        searchSourceBuilder.sort("publishTime", SortOrder.DESC);
        //分页查询
        searchSourceBuilder.size(2);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        //序列化为对象
        //分页查询下一页数据
        log.info("=====================下一页============================");
        SearchRequest searchRequest2 = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder2 = new SearchSourceBuilder();
        //排序条件
        searchSourceBuilder2.sort("id", SortOrder.ASC);
        searchSourceBuilder2.sort("publishTime", SortOrder.DESC);

        //存储上一次分页的sort信息
        searchSourceBuilder2.searchAfter(hits[hits.length - 1].getSortValues());
        searchSourceBuilder2.size(2);
        searchRequest2.source(searchSourceBuilder2);
        search = restHighLevelClient.search(searchRequest2, RequestOptions.DEFAULT);
    }

    // 智能提示
    public List<String> suggest(String indexName, String keyword) {
        CompletionSuggestionBuilder csb = SuggestBuilders.completionSuggestion("name_completion")
                .prefix(keyword, Fuzziness.ONE).skipDuplicates(true).size(10);
        //.analyzer("ik_max_word");
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        String suggestName = "justASuggestName";
        suggestBuilder.addSuggestion(suggestName, csb);
        SearchRequest request = new SearchRequest(indexName).source(new SearchSourceBuilder().suggest(suggestBuilder));
        SearchResponse response;
        try {
            log.info("es search param: {}", request.source().toString());
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> suggest
                    = response.getSuggest().getSuggestion(suggestName);
            //处理返回
            List<? extends Suggest.Suggestion.Entry.Option> options = suggest.getEntries().stream().map(Suggest.Suggestion.Entry::getOptions).findFirst().orElse(new ArrayList<>());
            List<String> result = new ArrayList<>();
            for (Suggest.Suggestion.Entry.Option option : options) {
                result.add(option.getText().toString());
            }
            return result;
        } catch (IOException e) {
            log.error("es search error", e);
        }
        return new ArrayList<>();
    }

    /**
     * 使用分词器对搜索内容进行分词
     *
     * @param index
     * @param keyword
     * @return
     */
    public List<String> splitWord(String index, String keyword) {
        AnalyzeRequest request = AnalyzeRequest.withIndexAnalyzer(index, "ik-max-word", keyword);
        AnalyzeResponse response;

        List<String> result = new ArrayList<>();
        try {
            response = restHighLevelClient.indices().analyze(request, RequestOptions.DEFAULT);
            for (AnalyzeResponse.AnalyzeToken token : response.getTokens()) {
                result.add(token.getTerm());
            }
        } catch (IOException e) {
            log.error("es Analyze failed!", e);
        }
        return result;
    }
    // endregion

    // region 模板
    public String createEsTemplate(String templateid) {
        Request scriptRequest = new Request("POST", "_scripts/" + templateid);
        String templateJsonString = Utils.getJsonByTemplate("template.json");
        scriptRequest.setJsonEntity(templateJsonString);
        RestClient restClient = restHighLevelClient.getLowLevelClient();
        try {
            Response response = restClient.performRequest(scriptRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "创建模板成功";
    }

    public String deleteEsTemplate(String templateid) {
        Request scriptRequest = new Request("DELETE", "_scripts/" + templateid);
        RestClient restClient = restHighLevelClient.getLowLevelClient();
        try {
            Response response = restClient.performRequest(scriptRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "创建模板成功";
    }

    public String getEsTemplate(String templateid, Map<String, Object> map) {
        SearchTemplateRequest request = new SearchTemplateRequest();
        request.setScriptType(ScriptType.STORED);
        request.setScript(templateid);
        request.setSimulate(true);
        request.setScriptParams(map);
        SearchTemplateResponse renderResponse = null;
        try {
            renderResponse = restHighLevelClient.searchTemplate(request, RequestOptions.DEFAULT);
            BytesReference source = renderResponse.getSource();
            return source.utf8ToString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public List<Map<String, Object>> useEsTemplate(String topic, String templateid, Map<String, Object> map) {
        SearchTemplateRequest request = new SearchTemplateRequest();
        request.setRequest(new SearchRequest(topic));
        request.setScriptType(ScriptType.STORED);
        request.setScript(templateid);
        request.setScriptParams(map);
        //request.setSimulate(true);
        SearchTemplateResponse response = null;
        try {
            response = restHighLevelClient.searchTemplate(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchResponse searchResponse = response.getResponse();
        //BytesReference source = response.getSource();
        //打印出储存的模板
        //System.out.println(source.utf8ToString());
        SearchHits searchHits = searchResponse.getHits();
        List<Map<String, Object>> maps = setSearchResponse(searchResponse, "");
        return maps;
        //return null;
    }

    public List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField) {
        //解析结果
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, HighlightField> high = hit.getHighlightFields();
            HighlightField title = high.get(highlightField);
            hit.getSourceAsMap().put("id", hit.getId());
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();//原来的结果
            //解析高亮字段,将原来的字段换为高亮字段
            if (title != null) {
                Text[] texts = title.fragments();
                String nTitle = "";
                for (Text text : texts) {
                    nTitle += text;
                }
                //替换
                sourceAsMap.put(highlightField, nTitle);
            }
            list.add(sourceAsMap);
        }
        return list;
    }
    // endregion

    // region 辅助方法
    private JSONObject objectToJSONObject(Student student) {
        JSONObject jsonObject = (JSONObject) JSON.toJSON(student);
        jsonObject.forEach((key, value) -> {
            if (value != null && value.getClass() == Date.class) {
                jsonObject.put(key, ((Date) value).getTime());
            }
        });
        jsonObject.put("_class", student.getClass().getTypeName());
        return jsonObject;
    }
    // endregion
}