package com.zjy.esdemo.service.impl;

import com.zjy.esdemo.dao.StudentDao;
import com.zjy.esdemo.po.Address;
import com.zjy.esdemo.po.Interest;
import com.zjy.esdemo.po.Student;
import com.zjy.esdemo.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentDao studentDao;

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    public void createIndex() throws Exception {
        // 创建索引
        elasticsearchRestTemplate.indexOps(Student.class).create();

        // 创建mapping
        Document mapping = elasticsearchRestTemplate.indexOps(Student.class).createMapping(Student.class);
        boolean brue = elasticsearchRestTemplate.indexOps(Student.class).putMapping(mapping);
        System.out.println(brue);
        System.out.println(mapping);
    }

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
        List scores = new ArrayList<>();
        scores.add(67.2);
        scores.add(27.2);
        scores.add(56.2);
        Address address = new Address();
        address.setProvince("湖北省");
        address.setCity("武汉市");
        address.setArea("高新区");

        List<Interest> list = new ArrayList<>();
        Interest interest = new Interest();
        interest.setCode("1");
        interest.setName("看书");
        list.add(interest);
        interest = new Interest();
        interest.setCode("2");
        interest.setName("打游戏");
        list.add(interest);

        Student student = new Student(1L, "刘伯", 21, scores, new Date(2022 - 1900, Calendar.JANUARY, 2, 3, 4, 5));
        student.setAddress(address);
        student.setInterests(list);
        studentDao.save(student);

        student = new Student(2L, "刘思想", 35, scores, new Date(2022 - 1900, Calendar.JANUARY, 3, 7, 5, 35));
        student.setAddress(address);
        student.setInterests(list);
        studentDao.save(student);

        student = new Student(3L, "王皮皮", 45, scores, new Date(2022 - 1900, Calendar.JANUARY, 4, 8, 24, 45));
        student.setAddress(address);
        student.setInterests(list);
        studentDao.save(student);

        student = new Student(4L, "王二丫", 23, scores, new Date(2022 - 1900, Calendar.JANUARY, 5, 19, 54, 32));
        student.setAddress(address);
        student.setInterests(list);
        studentDao.save(student);

        student = new Student(5L, "王铁蛋", 51, scores, new Date(2022 - 1900, Calendar.JANUARY, 6, 12, 33, 18));
        student.setAddress(address);
        student.setInterests(list);
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


    public void testFindByTitle() throws Exception {
        List<Student> list = studentDao.findByName("外交部");
        list.stream().forEach(a -> System.out.println(a));
    }

    public void testFindByTitleOrContent() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        studentDao.findByNameOrDesc("外交部", "美国", pageable).forEach(a -> System.out.println(a));
        /*list.stream().forEach(a-> System.out.println(a));*/
    }

    public void testNativeSearchQuery() throws Exception {
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(queryStringQuery("我想进外交部")
                .defaultField("title")).withPageable(PageRequest.of(0, 5)).build();
        elasticsearchRestTemplate.search(query, Student.class).stream().forEach(a -> System.out.println(a));
    }

    // https://blog.csdn.net/tianyaleixiaowu/article/details/77965257
    public Object singleTitle(String word, @PageableDefault Pageable pageable) {
        //使用queryStringQuery完成单字符串查询
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(queryStringQuery(word)).withPageable(pageable).build();
        return elasticsearchRestTemplate.search(searchQuery, Student.class);
    }

    public Object singlePost(String word, @PageableDefault(sort = "weight", direction = Sort.Direction.DESC) Pageable pageable) {
        //使用queryStringQuery完成单字符串查询
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(queryStringQuery(word)).withPageable(pageable).build();
        return elasticsearchRestTemplate.search(searchQuery, Student.class);
    }

    public Object singleMatch(String content, Integer userId, @PageableDefault Pageable pageable) {
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("content", content)).withPageable(pageable).build();
        //        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("userId", userId)).withPageable(pageable).build();
        return elasticsearchRestTemplate.search(searchQuery, Student.class);
    }

    public Object singlePhraseMatch(String content, @PageableDefault Pageable pageable) {
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(matchPhraseQuery("content", content)).withPageable(pageable).build();
        return elasticsearchRestTemplate.search(searchQuery, Student.class);
    }

    public Object singlePhraseMatch2(String content, @PageableDefault Pageable pageable) {
        //        Query searchQuery = new NativeSearchQueryBuilder().withQuery(matchPhraseQuery("content", content)).withPageable(pageable).build();少匹配一个分词也OK、
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(matchPhraseQuery("content", content).slop(2)).withPageable(pageable).build();
        return elasticsearchRestTemplate.search(searchQuery, Student.class);
    }

    public Object singleTerm(Integer userId, @PageableDefault Pageable pageable) {
        //不对传来的值分词，去找完全匹配的
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("userId", userId)).withPageable(pageable).build();
        return elasticsearchRestTemplate.search(searchQuery, Student.class);
    }

    public Object singleUserId (String title, @PageableDefault(sort = "weight", direction = Sort.Direction.DESC) Pageable pageable){
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(multiMatchQuery(title, "title", "content")).withPageable(pageable).build();
        return elasticsearchRestTemplate.search(searchQuery, Student.class);
    }

    public Object contain (String title) {
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("title", title).operator(Operator.AND)).build();
        return elasticsearchRestTemplate.search(searchQuery, Student.class);
    }
    public Object contain2 (String title){
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("title", title).operator(Operator.AND).minimumShouldMatch("75%")).build();
        return elasticsearchRestTemplate.search(searchQuery, Student.class);
    }

    public Object bool (String title, Integer userId, Integer weight){
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(
                boolQuery().
                        must(termQuery("userId", userId)).
                        should(rangeQuery("weight").lt(weight)).
                        must(matchQuery("title", title))).
                build();
        return elasticsearchRestTemplate.search(searchQuery, Student.class);
    }
}