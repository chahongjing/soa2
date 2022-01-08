package com.zjy.esdemo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zjy.esdemo.dao.StudentDao;
import com.zjy.esdemo.po.Student;
import com.zjy.esdemo.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    @Resource
    private EsUtils esUtils;

    @Autowired
    StudentDao studentDao;

    @Override
    public List<Student> findAll() {
        Iterable<Student> all = studentDao.findAll();
        List<Student> list = new ArrayList<>();
        for (Student testPo : all) {
            list.add(testPo);
        }
        return list;
    }

    @Override
    public List<Student> findByName(String text) {
        return studentDao.findByName(text);
    }

    @Override
    public void saveTest() {
        List scores= new ArrayList<>();
        scores.add(67.2);
        scores.add(27.2);
        scores.add(56.2);

        studentDao.save(new Student(1L, "刘伯", 21, scores, new Date(2022 - 1900, 1 - 1, 2, 3, 4, 5)));
        studentDao.save(new Student(2L, "刘思想", 35, scores, new Date(2022 - 1900, 1 - 1, 3, 7, 5, 35)));
        studentDao.save(new Student(3L, "王皮皮", 45, scores, new Date(2022 - 1900, 1 - 1, 4, 8, 24, 45)));
        studentDao.save(new Student(4L, "王二丫", 23, scores, new Date(2022 - 1900, 1 - 1, 5, 19, 54, 32)));
        studentDao.save(new Student(5L, "王铁蛋", 51, scores, new Date(2022 - 1900, 1 - 1, 6, 12, 33, 18)));
    }

    @Override
    public void save(Student bean) {
        studentDao.save(bean);
    }

    @Override
    public void deleteAll() {
        studentDao.deleteAll();
    }

    @Override
    public void createIndex(String index) {
        try {
            esUtils.createIndex(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteIndex(String index) {
        try {
            esUtils.deleteIndex(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //查询
    public List<Student> search() {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("name", "王"))
                .must(QueryBuilders.rangeQuery("birthday").gte(new Date(2022 - 1900, 1 - 1, 4, 8, 24, 45).getTime()))
                .must(QueryBuilders.rangeQuery("birthday").lte(new Date(2022 - 1900, 1 - 1, 5, 19, 54, 32).getTime()));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
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
            searchResponse = esUtils.searchBySearchSourceBuilde("student_index", searchSourceBuilder);
        } catch (Exception e) {
            log.error("es search error", e);
            throw new RuntimeException(e.getMessage());
        }
        SearchHit[] hitsArr = searchResponse.getHits().getHits();
        log.info("es search took: {}", searchResponse.getTook());
        List<Student> studentList = new ArrayList<>();
        for (SearchHit hit : hitsArr) {
            Student student = new Student();
            JSONObject source = JSONObject.parseObject(hit.getSourceAsString());
            if(!CollectionUtils.isEmpty(hit.getHighlightFields())) {
                HighlightField highlightField = hit.getHighlightFields().get("name");
                if(highlightField != null) {
                    student.setName(Arrays.stream(highlightField.getFragments()).map(Text::toString).collect(Collectors.joining("")));
                } else {
                    student.setName(source.getString("name"));
                }
            } else {
                student.setName(source.getString("name"));
            }
            long birthday = Long.parseLong(source.getString("birthday"));
            student.setBirthday(new Date(birthday));
            studentList.add(student);
        }
        return studentList;
    }

    //批量插入
    public void bulkInsert(List<Student> studentList) {
        List<String> jsonList = new ArrayList<>();
        for (Student student : studentList) {
            // student 转为 Json字符串
            jsonList.add(JSONObject.toJSONString(student));
        }
        BulkResponse bulkResponse = esUtils.bulkInsert(jsonList, "student_index");
    }
}
