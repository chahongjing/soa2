package com.zjy.esdemo.service.impl;

import com.zjy.esdemo.dao.StudentDao;
import com.zjy.esdemo.po.Address;
import com.zjy.esdemo.po.Interest;
import com.zjy.esdemo.po.Student;
import com.zjy.esdemo.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public boolean createIndex() {
        // 创建索引
        elasticsearchRestTemplate.indexOps(Student.class).create();

        // 创建mapping
        Document mapping = elasticsearchRestTemplate.indexOps(Student.class).createMapping(Student.class);
        boolean brue = elasticsearchRestTemplate.indexOps(Student.class).putMapping(mapping);
        System.out.println(brue);
        System.out.println(mapping);
        return brue;
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


    public void testFindByTitleOrContent() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        studentDao.findByNameOrDesc("外交部", "美国", pageable).forEach(System.out::println);
    }

    @Override
    public List<Student> testNativeSearchQuery() {
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(queryStringQuery("黄金")
                .defaultField("name")).withPageable(PageRequest.of(0, 5)).build();
        return querySearch(query);
    }

    // https://blog.csdn.net/tianyaleixiaowu/article/details/77965257
    public List<Student> singleTitle(String name, @PageableDefault Pageable pageable) {
        //使用queryStringQuery完成单字符串查询
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(queryStringQuery(name)).withPageable(pageable).build();
        return querySearch(searchQuery);
    }

    public Object singlePost(String name, @PageableDefault(sort = "age", direction = Sort.Direction.DESC) Pageable pageable) {
        //使用queryStringQuery完成单字符串查询
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(queryStringQuery(name)).withPageable(pageable).build();
        return querySearch(searchQuery);
    }

    public List<Student> singleMatch(String name, @PageableDefault Pageable pageable) {
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("name", name)).withPageable(pageable).build();
        return querySearch(searchQuery);
    }

    public List<Student> singlePhraseMatch(String name, @PageableDefault Pageable pageable) {
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(matchPhraseQuery("name", name)).withPageable(pageable).build();
        return querySearch(searchQuery);
    }

    public List<Student> singlePhraseMatch2(String name, @PageableDefault Pageable pageable) {
        //        Query searchQuery = new NativeSearchQueryBuilder().withQuery(matchPhraseQuery("content", content)).withPageable(pageable).build();少匹配一个分词也OK、
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(matchPhraseQuery("name", name).slop(2)).withPageable(pageable).build();
        return querySearch(searchQuery);
    }

    public List<Student> singleTerm(String name, @PageableDefault Pageable pageable) {
        //不对传来的值分词，去找完全匹配的
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("name", name)).withPageable(pageable).build();
        return querySearch(searchQuery);
    }

    public List<Student> singleUserId (String title, @PageableDefault(sort = "age", direction = Sort.Direction.DESC) Pageable pageable){
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(multiMatchQuery(title, "name", "desc")).withPageable(pageable).build();
        return querySearch(searchQuery);
    }

        public List<Student> contain (String name) {
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("name", name).operator(Operator.AND)).build();
        return querySearch(searchQuery);
    }
    public List<Student> contain2 (String name){
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("name", name).operator(Operator.AND).minimumShouldMatch("75%")).build();
        return querySearch(searchQuery);
    }

    /**
     * must和should混合查询
     * @param name
     * @param desc
     * @param age
     * @return
     */
    public List<Student> bool (String name, String desc, Integer age){
        BoolQueryBuilder shouldBuilder = new BoolQueryBuilder();
        shouldBuilder.should(rangeQuery("age").lt(age).gt(3).includeLower(true).includeUpper(true));

        Query searchQuery = new NativeSearchQueryBuilder().withQuery(
                boolQuery().
                        must(matchQuery("name", name)).
                        must(termQuery("desc", desc)).
                        must(shouldBuilder)).
//                        should(rangeQuery("age").lt(age).gt(3))).
                build();
        return querySearch(searchQuery);
    }

    List<Student> querySearch(Query query) {
        String queryStr = ((NativeSearchQuery) query).getQuery().toString();
        queryStr = "{\"query\":" + queryStr + "}";
        log.info("query String\r{}", queryStr);
        SearchHits<Student> hitsList = elasticsearchRestTemplate.search(query, Student.class);
        List<Student> list = hitsList.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        return list;
    }
}