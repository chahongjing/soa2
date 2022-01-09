package com.zjy.esdemo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zjy.esdemo.dao.StudentDao;
import com.zjy.esdemo.po.Address;
import com.zjy.esdemo.po.Student;
import com.zjy.esdemo.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
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
        Address address = new Address();
        address.setProvince("湖北省");
        address.setCity("武汉市");
        address.setArea("高新区");

        Student student = new Student(1L, "刘伯", 21, scores, new Date(2022 - 1900, Calendar.JANUARY, 2, 3, 4, 5));
        student.setAddress(address);
        studentDao.save(student);

        student = new Student(2L, "刘思想", 35, scores, new Date(2022 - 1900, Calendar.JANUARY, 3, 7, 5, 35));
        student.setAddress(address);
        studentDao.save(student);

        student = new Student(3L, "王皮皮", 45, scores, new Date(2022 - 1900, Calendar.JANUARY, 4, 8, 24, 45));
        student.setAddress(address);
        studentDao.save(student);

        student = new Student(4L, "王二丫", 23, scores, new Date(2022 - 1900, Calendar.JANUARY, 5, 19, 54, 32));
        student.setAddress(address);
        studentDao.save(student);

        student = new Student(5L, "王铁蛋", 51, scores, new Date(2022 - 1900, Calendar.JANUARY, 6, 12, 33, 18));
        student.setAddress(address);
        studentDao.save(student);
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

    @Override
    public void update(String index, long id, int age) {
        esUtils.updateDoc2(index, String.valueOf(id), age);
    }

    @Override
    public void insertDoc(String index) {
        Student student = new Student(6L, "黄金", 65, new ArrayList<>(), new Date(2022 - 1900, Calendar.FEBRUARY, 5, 23, 52, 18));

        Address address = new Address();
        address.setProvince("北京市");
        address.setCity("北京市");
        address.setArea("海淀区");
        student.setAddress(address);
        esUtils.insertDoc(index, student.getStudentId().toString(), student);
    }

    @Override
    public void deleteDoc(String index, String id) {
        esUtils.deleteDoc(index, id);
    }

    @Override
    public void batchInsertDoc(String index) {
        List<Student> list = new ArrayList<>();
        Address address = new Address();
        address.setProvince("湖北省");
        address.setCity("襄阳市");
        address.setArea("南漳县");
        Student student = new Student(7L, "黄金", 65, new ArrayList<>(), new Date(2022 - 1900, Calendar.APRIL, 5, 23, 52, 18));
        student.setAddress(address);
        list.add(student);
        student = new Student(8L, "白银", 34, new ArrayList<>(), new Date(2022 - 1900, Calendar.APRIL, 27, 6, 29, 41));
        student.setAddress(address);
        list.add(student);
        esUtils.bulkInsert(index, list);
//        esUtils.bulkIndex(index, list);
    }


    //查询
    public List<Student> search() {
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
            searchResponse = esUtils.searchBySearchSourceBuilde("student_index", searchSourceBuilder);
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
}
